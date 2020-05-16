package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.ErrorMessage;
import com.jpmorgan.ses.models.FieldError;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.jpmorgan.ses.validator.ValidationUtils.getFieldError;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FieldValidator<T, R> {
    private String fieldCode;
    private String errorCode;
    private String message;
    private Function<T, R> getter;
    private Predicate<R> check;

    public FieldError validate(R value, String objectName){
        if(check.test(value)){
            return null;
        } else {
            return getFieldError(errorCode, message, fieldCode, objectName);
        }
    }

    public Function<T, R> getter(){
        return getter;
    }

    public static <T, R> FieldValidator<T, R> field(String fieldCode, Function<T, R> getter){
        FieldValidator<T,R> fieldValidator = new FieldValidator<>();
        fieldValidator.fieldCode = fieldCode;
        fieldValidator.getter = getter;
        return fieldValidator;
    }

    public FieldValidator<T, R> mandatory() {
        this.check = v -> {
            if(v instanceof  String){
                return isNotBlank((String) v);
            } else {
                return Objects.nonNull(v);
            }
        };
        this.errorCode = ErrorMessage.VAL_ERR_FIELD_MANDATORY.getCode();
        this.message = String.format(ErrorMessage.VAL_ERR_FIELD_MANDATORY.getMessage(), fieldCode);
        return this;
    }
}
