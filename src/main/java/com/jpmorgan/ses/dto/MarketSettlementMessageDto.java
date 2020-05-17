package com.jpmorgan.ses.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
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