package com.fixit.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordEqualFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordEqualField {

    String message() default "Passwords must be equal";

    String passwordField();
    String passwordRepeatField();

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
