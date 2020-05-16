package com.jpmorgan.ses;

public enum ErrorMessage {
    SSI_NOT_EXISTS("ERR_API_0001","SSI not exists"),
    VALIDATION_ERRORS("ERR_API_0002","Request have validation errors"),
    VAL_ERR_OBJECT_NAME_MANDATORY("ERR_API_0003","Object name mandatory"),
    VAL_ERR_FIELD_MANDATORY("ERR_API_0004","Field '%s' is mandatory");

    private String code;
    private String message;

    ErrorMessage(String code, String message){
        this.code=code;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
