package com.jpmorgan.ses.dto;

import lombok.Builder;
import lombok.Data;

/*created by Buddhi*/

@Builder
@Data
public class TradeRequestDto {
    private String tradeId;
    private String ssiCode;
    private String amount;
    private String currency;
    private String valueDate;
}
