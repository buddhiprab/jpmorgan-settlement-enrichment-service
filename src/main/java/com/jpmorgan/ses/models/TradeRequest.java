package com.jpmorgan.ses.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "api", name = "trade_request")
public class TradeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tradeId;
    private String ssiCode;
    private String amount;
    private String currency;
    private String valueDate;
    private Date creationTime;
}
