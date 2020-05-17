package com.jpmorgan.ses.dto;

import lombok.Builder;
import lombok.Data;

/*created by Buddhi*/

@Builder
@Data
public class FieldErrorDto {
    private String fieldCode;
    private String errorDesc;
}
