package dev.ilkerk.digitalwallet.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse<T> {
    private final T data;
    private final Boolean isSuccess;
    private final int messageCode;
    private final String message;
    private final String userMessage;
}

