package com.jpmorgan.ses.controllers;

import com.buddhi.blog.dto.PostDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.services.SettlementMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/market/settlement_message")
public class SettlementMessageController {
    @Autowired
    SettlementMessageService settlementMessageService;

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody TradeRequestDto tradeRequestDto){
        Long id = settlementMessageService.createNewMarketSettlementMessage(tradeRequestDto);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

}
