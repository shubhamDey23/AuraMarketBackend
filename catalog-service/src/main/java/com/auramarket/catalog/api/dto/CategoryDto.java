package com.auramarket.catalog.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private UUID id;
    private String tenantId;
    private UUID parentId;
    private String name;
    private String slug;
    private String description;
    private String icon;
    private Integer displayOrder;
    private Boolean isActive;
}
