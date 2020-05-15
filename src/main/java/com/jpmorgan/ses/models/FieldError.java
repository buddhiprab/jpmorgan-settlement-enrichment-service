package com.jpmorgan.ses.models;

import lombok.Data;

@Data
public class FieldError {
    private String fieldCode;
    private String errorDesc;
}
