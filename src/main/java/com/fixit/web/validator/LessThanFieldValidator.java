package com.fixit.web.validator;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LessThanFieldValidator implements ConstraintValidator<LessThanField, Object> {

    private String lessThanField;
    private String greaterThanField;

    @Override
    public void initialize(LessThanField constraintAnnotation) {
        this.lessThanField = constraintAnnotation.lessThanField();
        this.greaterThanField = constraintAnnotation.greaterThanField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object lessThanFieldValue = new BeanWrapperImpl(value).getPropertyValue(lessThanField);
        Object greaterThanFieldValue = new BeanWrapperImpl(value).getPropertyValue(greaterThanField);

        if(value == null){
            return false;
        }
        boolean isValid;
        try {
            Double lessThan = Double.valueOf(lessThanFieldValue.toString());
            Double greaterThan = Double.valueOf(greaterThanFieldValue.toString());

            isValid = lessThan < greaterThan;
        }catch (NullPointerException exception){
            isValid = false;
        }
        if(!isValid){
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate("minimum value must be less than maximum value")
                .addPropertyNode(lessThanField).addConstraintViolation();
        }


        return isValid;
    }
}
