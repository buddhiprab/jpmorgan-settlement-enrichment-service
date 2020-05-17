package com.jpmorgan.ses.exception;

import com.jpmorgan.ses.models.FieldError;

import java.util.List;

/*created by Buddhi*/

public class ApiException extends Exception {
    private String code;
    private List<FieldError> fieldErrors;

    public ApiException(String code, String msg, List<FieldError> fieldErrors) {
        super(msg);
        this.code = code;
        this.fieldErrors = fieldErrors;
    }

    public String getCode() {
        return code;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
