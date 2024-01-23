package com.aiservice.constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Repeatable(Buckets.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {BucketValidator.class})
public @interface Bucket {
    
    String message() default "";
    
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String selectedFieldName();
    
    String bucketName();


}
