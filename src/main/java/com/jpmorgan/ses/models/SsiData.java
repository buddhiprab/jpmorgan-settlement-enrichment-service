package com.jpmorgan.ses.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "api", name = "ssi_data")
public class SsiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ssiCode;
    private String payerAccNum;
    private String payerBank;
    private String receiverAccNum;
    private String receiverBank;
    private String info;
}