package com.auramarket.tenant.api.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFeatureRequest {
    private Boolean isEnabled;
    private String configurationJson;
}
