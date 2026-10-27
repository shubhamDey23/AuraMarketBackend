package com.auramarket.tenant.application.service;

import com.auramarket.tenant.api.dto.*;
import com.auramarket.tenant.api.dto.admin.*;
import com.auramarket.tenant.domain.exception.TenantNotFoundException;
import com.auramarket.tenant.domain.model.*;
import com.auramarket.tenant.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantBrandingRepository tenantBrandingRepository;
    private final TenantMenuRepository tenantMenuRepository;
    private final TenantFeatureRepository tenantFeatureRepository;
    private final TenantSettingRepository tenantSettingRepository;
    private final TenantCacheService tenantCacheService;

    @Transactional(readOnly = true)
    public StorefrontConfigResponse getStorefrontConfig(String tenantCode) {
        // 1. Check Redis cache
        Optional<StorefrontConfigResponse> cached = tenantCacheService.getStorefrontConfig(tenantCode);
        if (cached.isPresent()) {
            return cached.get();
        }

        // 2. Fetch from DB
        Tenant tenant = tenantRepository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with code: " + tenantCode));

        if (!"ACTIVE".equalsIgnoreCase(tenant.getStatus())) {
            throw new TenantNotFoundException("Tenant with code " + tenantCode + " is not ACTIVE");
        }

        TenantBranding branding = tenantBrandingRepository.findByTenantId(tenant.getId())
                .orElseGet(() -> TenantBranding.builder()
                        .primaryColor("#2F6B4F")
                        .secondaryColor("#8BAF91")
                        .accentColor("#C69A45")
                        .backgroundColor("#F8FAF7")
                        .surfaceColor("#FFFFFF")
                        .textColor("#1F2933")
                        .fontFamily("Inter, sans-serif")
                        .borderRadius("8px")
                        .build());

        List<TenantMenu> dbMenus = tenantMenuRepository.findByTenantIdAndIsVisibleTrueOrderByDisplayOrderAsc(tenant.getId());
        List<TenantFeature> dbFeatures = tenantFeatureRepository.findByTenantId(tenant.getId());
        List<TenantSetting> dbSettings = tenantSettingRepository.findByTenantId(tenant.getId());

        TenantInfoDto tenantInfo = mapToTenantInfoDto(tenant);
        BrandingDto brandingDto = mapToBrandingDto(branding);

        Map<String, Boolean> featuresMap = dbFeatures.stream()
                .collect(Collectors.toMap(
                        TenantFeature::getFeatureCode,
                        TenantFeature::getIsEnabled,
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));

        Map<String, String> settingsMap = dbSettings.stream()
                .collect(Collectors.toMap(
                        TenantSetting::getSettingKey,
                        s -> s.getSettingValue() != null ? s.getSettingValue() : "",
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new
                ));

        List<MenuItemDto> menuTree = buildMenuTree(dbMenus, featuresMap);

        StorefrontConfigResponse response = StorefrontConfigResponse.builder()
                .tenant(tenantInfo)
                .branding(brandingDto)
                .features(featuresMap)
                .menus(menuTree)
                .settings(settingsMap)
                .build();

        // 3. Cache response in Redis
        tenantCacheService.cacheStorefrontConfig(tenantCode, response);

        return response;
    }

    private List<MenuItemDto> buildMenuTree(List<TenantMenu> dbMenus, Map<String, Boolean> featuresMap) {
        Map<UUID, MenuItemDto> dtoMap = new LinkedHashMap<>();
        List<MenuItemDto> rootMenus = new ArrayList<>();

        // Map visible menus that satisfy feature requirements
        for (TenantMenu menu : dbMenus) {
            if (menu.getRequiredFeature() != null && !menu.getRequiredFeature().isBlank()) {
                Boolean isFeatureEnabled = featuresMap.getOrDefault(menu.getRequiredFeature(), false);
                if (!Boolean.TRUE.equals(isFeatureEnabled)) {
                    continue; // Skip menu if required feature is disabled
                }
            }

            MenuItemDto dto = MenuItemDto.builder()
                    .id(menu.getId())
                    .parentId(menu.getParentId())
                    .label(menu.getLabel())
                    .route(menu.getRoute())
                    .icon(menu.getIcon())
                    .displayOrder(menu.getDisplayOrder())
                    .isVisible(menu.getIsVisible())
                    .requiredFeature(menu.getRequiredFeature())
                    .openInNewTab(menu.getOpenInNewTab())
                    .children(new ArrayList<>())
                    .build();

            dtoMap.put(dto.getId(), dto);
        }

        // Build hierarchy
        for (MenuItemDto dto : dtoMap.values()) {
            if (dto.getParentId() != null && dtoMap.containsKey(dto.getParentId())) {
                dtoMap.get(dto.getParentId()).getChildren().add(dto);
            } else {
                rootMenus.add(dto);
            }
        }

        return rootMenus;
    }

    // =========================================================================
    // ADMIN SERVICE METHODS
    // =========================================================================

    @Transactional(readOnly = true)
    public List<TenantInfoDto> getAllTenants() {
        return tenantRepository.findAll().stream()
                .map(this::mapToTenantInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TenantInfoDto getTenantById(UUID tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));
        return mapToTenantInfoDto(tenant);
    }

    @Transactional
    public TenantInfoDto createTenant(CreateTenantRequest request) {
        if (tenantRepository.existsByTenantCode(request.getTenantCode())) {
            throw new IllegalArgumentException("Tenant code already exists: " + request.getTenantCode());
        }

        Tenant tenant = Tenant.builder()
                .tenantCode(request.getTenantCode())
                .name(request.getName())
                .slug(request.getSlug())
                .businessType(request.getBusinessType() != null ? request.getBusinessType() : "GENERAL")
                .status("ACTIVE")
                .defaultCurrency(request.getDefaultCurrency() != null ? request.getDefaultCurrency() : "INR")
                .defaultLocale(request.getDefaultLocale() != null ? request.getDefaultLocale() : "en-IN")
                .timezone(request.getTimezone() != null ? request.getTimezone() : "Asia/Kolkata")
                .build();

        Tenant saved = tenantRepository.save(tenant);
        return mapToTenantInfoDto(saved);
    }

    @Transactional
    public TenantInfoDto updateTenant(UUID tenantId, UpdateTenantRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        if (request.getName() != null) tenant.setName(request.getName());
        if (request.getBusinessType() != null) tenant.setBusinessType(request.getBusinessType());
        if (request.getStatus() != null) tenant.setStatus(request.getStatus());
        if (request.getDefaultCurrency() != null) tenant.setDefaultCurrency(request.getDefaultCurrency());
        if (request.getDefaultLocale() != null) tenant.setDefaultLocale(request.getDefaultLocale());
        if (request.getTimezone() != null) tenant.setTimezone(request.getTimezone());

        Tenant updated = tenantRepository.save(tenant);
        tenantCacheService.evictStorefrontConfig(updated.getTenantCode());
        return mapToTenantInfoDto(updated);
    }

    @Transactional
    public BrandingDto updateBranding(UUID tenantId, UpdateBrandingRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        TenantBranding branding = tenantBrandingRepository.findByTenantId(tenantId)
                .orElseGet(() -> TenantBranding.builder().tenantId(tenantId).build());

        if (request.getLogoUrl() != null) branding.setLogoUrl(request.getLogoUrl());
        if (request.getFaviconUrl() != null) branding.setFaviconUrl(request.getFaviconUrl());
        if (request.getPrimaryColor() != null) branding.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) branding.setSecondaryColor(request.getSecondaryColor());
        if (request.getAccentColor() != null) branding.setAccentColor(request.getAccentColor());
        if (request.getBackgroundColor() != null) branding.setBackgroundColor(request.getBackgroundColor());
        if (request.getSurfaceColor() != null) branding.setSurfaceColor(request.getSurfaceColor());
        if (request.getTextColor() != null) branding.setTextColor(request.getTextColor());
        if (request.getFontFamily() != null) branding.setFontFamily(request.getFontFamily());
        if (request.getBorderRadius() != null) branding.setBorderRadius(request.getBorderRadius());

        TenantBranding saved = tenantBrandingRepository.save(branding);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());
        return mapToBrandingDto(saved);
    }

    @Transactional(readOnly = true)
    public List<MenuItemDto> getTenantMenus(UUID tenantId) {
        List<TenantMenu> menus = tenantMenuRepository.findByTenantIdOrderByDisplayOrderAsc(tenantId);
        return menus.stream()
                .map(m -> MenuItemDto.builder()
                        .id(m.getId())
                        .parentId(m.getParentId())
                        .label(m.getLabel())
                        .route(m.getRoute())
                        .icon(m.getIcon())
                        .displayOrder(m.getDisplayOrder())
                        .isVisible(m.getIsVisible())
                        .requiredFeature(m.getRequiredFeature())
                        .openInNewTab(m.getOpenInNewTab())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public MenuItemDto addMenuItem(UUID tenantId, SaveMenuItemRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        TenantMenu menu = TenantMenu.builder()
                .tenantId(tenantId)
                .parentId(request.getParentId())
                .label(request.getLabel())
                .route(request.getRoute())
                .icon(request.getIcon())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isVisible(request.getIsVisible() != null ? request.getIsVisible() : true)
                .requiredFeature(request.getRequiredFeature())
                .openInNewTab(request.getOpenInNewTab() != null ? request.getOpenInNewTab() : false)
                .build();

        TenantMenu saved = tenantMenuRepository.save(menu);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());

        return MenuItemDto.builder()
                .id(saved.getId())
                .parentId(saved.getParentId())
                .label(saved.getLabel())
                .route(saved.getRoute())
                .icon(saved.getIcon())
                .displayOrder(saved.getDisplayOrder())
                .isVisible(saved.getIsVisible())
                .requiredFeature(saved.getRequiredFeature())
                .openInNewTab(saved.getOpenInNewTab())
                .build();
    }

    @Transactional
    public MenuItemDto updateMenuItem(UUID tenantId, UUID menuId, SaveMenuItemRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        TenantMenu menu = tenantMenuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found with ID: " + menuId));

        if (!menu.getTenantId().equals(tenantId)) {
            throw new IllegalArgumentException("Menu item does not belong to tenant ID: " + tenantId);
        }

        menu.setParentId(request.getParentId());
        menu.setLabel(request.getLabel());
        menu.setRoute(request.getRoute());
        menu.setIcon(request.getIcon());
        if (request.getDisplayOrder() != null) menu.setDisplayOrder(request.getDisplayOrder());
        if (request.getIsVisible() != null) menu.setIsVisible(request.getIsVisible());
        menu.setRequiredFeature(request.getRequiredFeature());
        if (request.getOpenInNewTab() != null) menu.setOpenInNewTab(request.getOpenInNewTab());

        TenantMenu updated = tenantMenuRepository.save(menu);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());

        return MenuItemDto.builder()
                .id(updated.getId())
                .parentId(updated.getParentId())
                .label(updated.getLabel())
                .route(updated.getRoute())
                .icon(updated.getIcon())
                .displayOrder(updated.getDisplayOrder())
                .isVisible(updated.getIsVisible())
                .requiredFeature(updated.getRequiredFeature())
                .openInNewTab(updated.getOpenInNewTab())
                .build();
    }

    @Transactional
    public void deleteMenuItem(UUID tenantId, UUID menuId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        tenantMenuRepository.deleteByTenantIdAndId(tenantId, menuId);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());
    }

    @Transactional(readOnly = true)
    public List<TenantFeature> getTenantFeatures(UUID tenantId) {
        return tenantFeatureRepository.findByTenantId(tenantId);
    }

    @Transactional
    public TenantFeature updateFeature(UUID tenantId, String featureCode, UpdateFeatureRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        TenantFeature feature = tenantFeatureRepository.findByTenantIdAndFeatureCode(tenantId, featureCode)
                .orElseGet(() -> TenantFeature.builder()
                        .tenantId(tenantId)
                        .featureCode(featureCode)
                        .build());

        if (request.getIsEnabled() != null) feature.setIsEnabled(request.getIsEnabled());
        if (request.getConfigurationJson() != null) feature.setConfigurationJson(request.getConfigurationJson());

        TenantFeature saved = tenantFeatureRepository.save(feature);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<TenantSetting> getTenantSettings(UUID tenantId) {
        return tenantSettingRepository.findByTenantId(tenantId);
    }

    @Transactional
    public TenantSetting updateSetting(UUID tenantId, String settingKey, UpdateSettingRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID: " + tenantId));

        TenantSetting setting = tenantSettingRepository.findByTenantIdAndSettingKey(tenantId, settingKey)
                .orElseGet(() -> TenantSetting.builder()
                        .tenantId(tenantId)
                        .settingKey(settingKey)
                        .build());

        if (request.getSettingValue() != null) setting.setSettingValue(request.getSettingValue());
        if (request.getValueType() != null) setting.setValueType(request.getValueType());

        TenantSetting saved = tenantSettingRepository.save(setting);
        tenantCacheService.evictStorefrontConfig(tenant.getTenantCode());
        return saved;
    }

    public void invalidateTenantCache(String tenantCode) {
        tenantCacheService.evictStorefrontConfig(tenantCode);
    }

    private TenantInfoDto mapToTenantInfoDto(Tenant tenant) {
        return TenantInfoDto.builder()
                .id(tenant.getId())
                .tenantCode(tenant.getTenantCode())
                .name(tenant.getName())
                .slug(tenant.getSlug())
                .businessType(tenant.getBusinessType())
                .status(tenant.getStatus())
                .currency(tenant.getDefaultCurrency())
                .locale(tenant.getDefaultLocale())
                .timezone(tenant.getTimezone())
                .build();
    }

    private BrandingDto mapToBrandingDto(TenantBranding branding) {
        return BrandingDto.builder()
                .logoUrl(branding.getLogoUrl())
                .faviconUrl(branding.getFaviconUrl())
                .primaryColor(branding.getPrimaryColor())
                .secondaryColor(branding.getSecondaryColor())
                .accentColor(branding.getAccentColor())
                .backgroundColor(branding.getBackgroundColor())
                .surfaceColor(branding.getSurfaceColor())
                .textColor(branding.getTextColor())
                .fontFamily(branding.getFontFamily())
                .borderRadius(branding.getBorderRadius())
                .build();
    }
}
