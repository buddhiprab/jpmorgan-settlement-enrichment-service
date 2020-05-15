package com.jpmorgan.ses.services;

import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.PayerPartyDto;
import com.jpmorgan.ses.dto.ReceiverPartyDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.exception.ApiException;
import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.models.TradeRequest;
import com.jpmorgan.ses.repositories.SsiDataRepository;
import com.jpmorgan.ses.repositories.TradeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class SettlementMessageService {
    @Autowired
    TradeRequestRepository tradeRequestRepository;
    @Autowired
    SsiDataRepository ssiDataRepository;

    @Transactional(rollbackOn = RuntimeException.class)
    public MarketSettlementMessageDto createNewMarketSettlementMessage(TradeRequestDto tradeRequestDto) throws ApiException {
        //validate trade request

        //save trade request
        createTradeRequest(tradeRequestDto);
        //get SSI record
        SsiData ssiData = ssiDataRepository.findBySsiCode(tradeRequestDto.getSsiCode()).get(0);

        //create market settlement message
        MarketSettlementMessageDto marketSettlementMessageDto = createMarketSettlementMessage(tradeRequestDto, ssiData);

        return marketSettlementMessageDto;
    }

    private MarketSettlementMessageDto createMarketSettlementMessage(TradeRequestDto tradeRequestDto, SsiData ssiData) {
        MarketSettlementMessageDto marketSettlementMessageDto = new MarketSettlementMessageDto();
        copyProperties(tradeRequestDto, marketSettlementMessageDto);
        marketSettlementMessageDto.setMessageId(UUID.randomUUID().toString());
        marketSettlementMessageDto.setAmount(new BigDecimal(tradeRequestDto.getAmount()));
        marketSettlementMessageDto.setSupportingInformation(ssiData.getInfo());

        PayerPartyDto payerPartyDto = new PayerPartyDto();
        payerPartyDto.setAccountNumber(ssiData.getPayerAccNum());
        payerPartyDto.setBankCode(ssiData.getPayerBank());

        marketSettlementMessageDto.setPayerParty(payerPartyDto);

        ReceiverPartyDto receiverPartyDto = new ReceiverPartyDto();
        receiverPartyDto.setAccountNumber(ssiData.getReceiverAccNum());
        receiverPartyDto.setBankCode(ssiData.getReceiverBank());

        marketSettlementMessageDto.setReceiverParty(receiverPartyDto);

        return marketSettlementMessageDto;
    }

    private Long createTradeRequest(TradeRequestDto tradeRequestDto){
        TradeRequest tradeRequest = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequest);
        tradeRequest.setCreationTime(new Date());

        TradeRequest tradeRequestDb = tradeRequestRepository.save(tradeRequest);
        return tradeRequestDb.getId();
    }


}
