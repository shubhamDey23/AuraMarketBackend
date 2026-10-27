package com.auramarket.tenant.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID parentId;
    private String label;
    private String route;
    private String icon;
    private Integer displayOrder;
    private Boolean isVisible;
    private String requiredFeature;
    private Boolean openInNewTab;
    @Builder.Default
    private List<MenuItemDto> children = new ArrayList<>();
}
