package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.models.FieldError;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*created by Buddhi*/

public class ValidationUtils {

    public static FieldError getFieldError(String errorCode, String message, String fieldCode, String objectName) {
        FieldError fieldError = new FieldError();
        fieldError.setErrorDesc(errorCode + ' ' + message);
        if (isNotBlank(objectName)) {
            fieldCode = objectName + "." + fieldCode;
        }
        fieldError.setFieldCode(fieldCode);
        return fieldError;
    }

    public static FieldError getFieldError(String errorDesc, String fieldCode) {
        FieldError fieldError = new FieldError();
        fieldError.setErrorDesc(errorDesc);
        fieldError.setFieldCode(fieldCode);
        return fieldError;
    }
}
