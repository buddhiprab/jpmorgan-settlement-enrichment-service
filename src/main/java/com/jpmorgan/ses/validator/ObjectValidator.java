package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.enums.ErrorMessage;
import com.jpmorgan.ses.models.FieldError;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jpmorgan.ses.validator.ValidationUtils.getFieldError;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ObjectValidator<T> {
    private List<FieldValidator> fieldValidators = new LinkedList<>();
    private final String objectName;

    public ObjectValidator(String objectName) {
        this.objectName = objectName;
    }

    public <R> ValidationResult validateObject(T target, ValidationResult previous) {
        if (target == null && isNotBlank(objectName)) {
            FieldError fieldError = getFieldError(ErrorMessage.VAL_ERR_OBJECT_NAME_MANDATORY.getCode() + " " + objectName + " " + ErrorMessage.VAL_ERR_OBJECT_NAME_MANDATORY.getMessage(), objectName);
            previous.getFieldErrors().add(fieldError);
            return previous;
        }

        List<FieldError> errors = fieldValidators.stream().map(validator -> {
            R value = (R) validator.getter().apply(target);
            return validator.validate(value, objectName);
        }).filter(Objects::nonNull).collect(Collectors.toList());
        if (!errors.isEmpty()) {
            previous.getFieldErrors().addAll(errors);
        }
        return previous;
    }

    public <R> FieldValidator<T, R> field(String fieldCode, Function<T, R> getter) {
        FieldValidator<T, R> validator = FieldValidator.field(fieldCode, getter);
        fieldValidators.add(validator);
        return validator;
    }
}
