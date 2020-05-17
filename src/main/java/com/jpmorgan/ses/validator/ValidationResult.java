package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.models.FieldError;

import java.util.LinkedList;
import java.util.List;

public class ValidationResult {
    private boolean validationStatus;
    private List<FieldError> fieldErrors;

    public boolean hasValidationErrors() {
        return !fieldErrors.isEmpty();
    }

    public List<FieldError> getFieldErrors() {
        if (fieldErrors == null) {
            fieldErrors = new LinkedList<>();
        }
        return fieldErrors;
    }

    public static ValidationResult initial() {
        ValidationResult initial = new ValidationResult();
        initial.fieldErrors = new LinkedList<>();
        return initial;
    }
}
