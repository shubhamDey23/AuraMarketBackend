package com.auramarket.tenant.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BrandingDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String logoUrl;
    private String faviconUrl;
    private String primaryColor;
    private String secondaryColor;
    private String accentColor;
    private String backgroundColor;
    private String surfaceColor;
    private String textColor;
    private String fontFamily;
    private String borderRadius;
}
