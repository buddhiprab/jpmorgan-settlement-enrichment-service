package com.jpmorgan.ses.dto;

import lombok.Builder;
import lombok.Data;

/*created by Buddhi*/

@Builder
@Data
public class PayerPartyDto {
    private String accountNumber;
    private String bankCode;
}
