package com.aiservice.constraint;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Repeatable(Conditionals.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConditionalValidator.class})
public @interface Conditional {
    
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] conditionalFieldNames();

}
