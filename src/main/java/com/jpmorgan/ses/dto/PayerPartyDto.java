package com.jpmorgan.ses.dto;

import lombok.Data;

@Data
public class PayerPartyDto {
    private String accountNumber;
    private String bankCode;
}
