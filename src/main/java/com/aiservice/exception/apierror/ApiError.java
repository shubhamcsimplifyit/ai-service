package com.aiservice.exception.apierror;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.aiservice.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError {

    private Integer status=0;

    private HttpStatus httpStatus;
    
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_TIME_FORMATTER_UI,timezone = "IST")
    private LocalDateTime timestamp;
    
    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.httpStatus = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = message;
        this.debugMessage = ex.getMessage();
    }
    
    public ApiError(HttpStatus status, String message,String path, Throwable ex) {
        this();
        this.httpStatus = status;
        this.message = message;
        this.path = path;
        this.debugMessage = ex.getMessage();
    }


    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    private void addValidationError(FieldError fieldError) {
        this.addValidationError(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    private void addValidationError(ObjectError objectError) {
        this.addValidationError(
                        objectError.getObjectName(),
                        objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a @Validated validation fails. Refer: CustomerPostAddAllController.java
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(
                        cv.getRootBeanClass().getSimpleName(),
                        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                        cv.getInvalidValue(),
                        cv.getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }

}
