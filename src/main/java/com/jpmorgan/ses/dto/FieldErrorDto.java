package com.jpmorgan.ses.dto;

import lombok.Data;

@Data
public class FieldErrorDto {
    private String fieldCode;
    private String errorDesc;
}
