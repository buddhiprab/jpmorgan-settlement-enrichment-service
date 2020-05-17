package com.jpmorgan.ses.dto;

import lombok.Data;

import java.util.List;

/*created by Buddhi*/

@Data
public class ErrorResponseDto {
    private String errorCode;
    private String errorMessage;
    private List<FieldErrorDto> fieldErrors;
}
