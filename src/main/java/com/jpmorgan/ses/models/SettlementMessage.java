package com.jpmorgan.ses.models;

import lombok.Data;

import javax.persistence.*;

/*created by Buddhi*/

@Data
@Entity
@Table(schema = "api", name = "settlement_message")
public class SettlementMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message_id;
    private String message;
}
