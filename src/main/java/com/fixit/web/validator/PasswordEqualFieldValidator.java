package com.fixit.web.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordEqualFieldValidator implements ConstraintValidator<PasswordEqualField, Object> {

    private String passwordField;
    private String passwordRepeatField;

    @Override
    public void initialize(PasswordEqualField constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.passwordRepeatField = constraintAnnotation.passwordRepeatField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordFieldValue = new BeanWrapperImpl(value).getPropertyValue(passwordField);
        Object passwordRepeatFieldValue = new BeanWrapperImpl(value).getPropertyValue(passwordRepeatField);

        if(value == null){
            return false;
        }
        boolean isValid;
        try {
            String password = passwordFieldValue.toString();
            String passwordRepeat = passwordRepeatFieldValue.toString();

            isValid = password.equals(passwordRepeat);
        }catch (NullPointerException exception){
            isValid = false;
        }
        if(!isValid){
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Provided passwords must be equal")
                    .addPropertyNode(passwordRepeatField).addConstraintViolation();
        }


        return isValid;
    }
}
