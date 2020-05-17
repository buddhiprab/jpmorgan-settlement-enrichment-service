package com.jpmorgan.ses.enums;

public enum ErrorMessage {
    WE_CANT_PROCESS_REQUEST("ERR_API_0001", "Sorry we can't process your request"),
    INVALID_REQUEST_JSON("ERR_API_0002", "invalid Reqeust JSON"),
    SSI_NOT_EXISTS("ERR_API_VAL", "invalid SSI Code"),
    TRADE_ID_NOT_EXISTS("ERR_API_VAL", "invalid Trade Id"),
    VALIDATION_ERRORS("ERR_API_VAL", "Request have validation errors"),
    VAL_ERR_OBJECT_NAME_MANDATORY("ERR_API_VAL", "Object name mandatory"),
    VAL_ERR_FIELD_MANDATORY("ERR_API_VAL", "Field '%s' is mandatory"),
    VAL_ERR_FIELD_INVALID_FORMAT("ERR_API_VAL", "Invalid format, value must be a number with optional 2 decimals");

    private String code;
    private String message;

    ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
