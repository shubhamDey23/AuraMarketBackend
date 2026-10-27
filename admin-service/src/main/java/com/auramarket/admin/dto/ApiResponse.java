package com.auramarket.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private String errorCode;
    private T data;
    @Builder.Default
    private OffsetDateTime timestamp = OffsetDateTime.now();
    private String path;
    private String correlationId;
}
