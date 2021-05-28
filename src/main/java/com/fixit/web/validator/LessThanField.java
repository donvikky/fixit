package com.fixit.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = LessThanFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LessThanField {

    String message() default "Minimum amount must be less than maximum.";

    String lessThanField();
    String greaterThanField();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
