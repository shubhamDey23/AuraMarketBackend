package com.auramarket.tenant.api.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaveMenuItemRequest {
    private UUID parentId;

    @NotBlank(message = "Label is required")
    private String label;

    @NotBlank(message = "Route is required")
    private String route;

    private String icon;
    private Integer displayOrder;
    private Boolean isVisible;
    private String requiredFeature;
    private Boolean openInNewTab;
}
