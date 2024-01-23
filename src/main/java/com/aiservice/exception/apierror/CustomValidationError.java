package com.aiservice.exception.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class CustomValidationError implements ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public CustomValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}