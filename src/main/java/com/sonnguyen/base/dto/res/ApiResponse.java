package com.sonnguyen.base.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

}