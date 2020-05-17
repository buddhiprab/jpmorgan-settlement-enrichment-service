package com.jpmorgan.ses.models;

import lombok.Data;

/*created by Buddhi*/

@Data
public class FieldError {
    private String fieldCode;
    private String errorDesc;
}
