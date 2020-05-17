package com.jpmorgan.ses.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PayerPartyDto {
    private String accountNumber;
    private String bankCode;
}
