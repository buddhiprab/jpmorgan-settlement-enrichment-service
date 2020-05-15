package com.jpmorgan.ses.dto;

import lombok.Data;

@Data
public class TradeRequestDto {
    private String tradeId;
    private String ssiCode;
    private String amount;
    private String currency;
    private String valueDate;
}
