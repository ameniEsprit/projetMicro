package com.esprit.userservice.advice;

import com.esprit.userservice.entity.RoleType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class RoleTypeValidator implements ConstraintValidator<ValidRoleType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            RoleType roleType = RoleType.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
