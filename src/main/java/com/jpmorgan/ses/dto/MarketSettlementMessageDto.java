package com.jpmorgan.ses.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MarketSettlementMessageDto {
    private String tradeId;
    private String messageId;
    private BigDecimal amount;
    private String valueDate;
    private String currency;
    private PayerPartyDto payerParty;
    private ReceiverPartyDto receiverParty;
    private String supportingInformation;
}