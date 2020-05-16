package com.jpmorgan.ses.services;

import com.jpmorgan.ses.ErrorMessage;
import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.PayerPartyDto;
import com.jpmorgan.ses.dto.ReceiverPartyDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.exception.ApiException;
import com.jpmorgan.ses.models.SettlementMessage;
import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.models.TradeRequest;
import com.jpmorgan.ses.repositories.SettlementMessageRepository;
import com.jpmorgan.ses.repositories.SsiDataRepository;
import com.jpmorgan.ses.repositories.TradeRequestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.springframework.beans.BeanUtils.copyProperties;

@Slf4j
@Service
public class SettlementMessageService {
    @Autowired
    ValidationService validationService;
    @Autowired
    TradeRequestRepository tradeRequestRepository;
    @Autowired
    SsiDataRepository ssiDataRepository;
    @Autowired
    SettlementMessageRepository settlementMessageRepository;

    @Transactional(rollbackOn = RuntimeException.class)
    public MarketSettlementMessageDto createNewMarketSettlementMessage(TradeRequestDto tradeRequestDto) throws ApiException {
        log.info("Start creating new MarketSettlementMessage");
        //check ssi record exists
        SsiData ssiData = ssiDataRepository.findBySsiCode(tradeRequestDto.getSsiCode());
        if(ssiData==null){
            throw new ApiException(ErrorMessage.SSI_NOT_EXISTS.getCode(),ErrorMessage.SSI_NOT_EXISTS.getMessage(),null);
        }

        //validate trade request
        validationService.validate(tradeRequestDto);
        log.info("TradeRequest validation success");

        //save trade request
        TradeRequest tradeRequestDb = persistTradeRequest(tradeRequestDto);
        //create enriched market settlement message
        MarketSettlementMessageDto marketSettlementMessageDto = convertToMarketSettlementMessage(tradeRequestDb, ssiData);
        //save market settlement message
        persistSettlementMessage(marketSettlementMessageDto);

        return marketSettlementMessageDto;
    }

    public MarketSettlementMessageDto getMarketSettlementMessage(String tradeId) throws ApiException {
        //get trade request
        TradeRequest tradeRequest = tradeRequestRepository.findByTradeId(tradeId).get(0);
        //get SSI record
        SsiData ssiData = ssiDataRepository.findBySsiCode(tradeRequest.getSsiCode());

        return convertToMarketSettlementMessage(tradeRequest, ssiData);
    }

    private SettlementMessage persistSettlementMessage(MarketSettlementMessageDto marketSettlementMessageDto) {
        SettlementMessage settlementMessage = new SettlementMessage();
        settlementMessage.setMessage_id(marketSettlementMessageDto.getMessageId());
        settlementMessage.setMessage(marketSettlementMessageDto.toString());
        return settlementMessageRepository.save(settlementMessage);
    }

    private TradeRequest persistTradeRequest(TradeRequestDto tradeRequestDto){
        TradeRequest tradeRequest = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequest);
        tradeRequest.setCreationTime(new Date());
        return tradeRequestRepository.save(tradeRequest);
    }

    private MarketSettlementMessageDto convertToMarketSettlementMessage(TradeRequest tradeRequest, SsiData ssiData) {
        MarketSettlementMessageDto marketSettlementMessageDto = new MarketSettlementMessageDto();
        marketSettlementMessageDto.setTradeId(tradeRequest.getTradeId());
        marketSettlementMessageDto.setValueDate(tradeRequest.getValueDate());
        marketSettlementMessageDto.setCurrency(tradeRequest.getCurrency());
        marketSettlementMessageDto.setMessageId(UUID.randomUUID().toString());
        marketSettlementMessageDto.setAmount(new BigDecimal(tradeRequest.getAmount()));
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
}
