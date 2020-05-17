package com.jpmorgan.ses.services;

import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.PayerPartyDto;
import com.jpmorgan.ses.dto.ReceiverPartyDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.enums.ErrorMessage;
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

        //validate trade request
        validationService.validate(tradeRequestDto);
        log.info("TradeRequest validation success");

        //check ssi record exists
        SsiData ssiData = ssiDataRepository.findBySsiCode(tradeRequestDto.getSsiCode());
        if (ssiData == null) {
            throw new ApiException(ErrorMessage.SSI_NOT_EXISTS.getCode(), ErrorMessage.SSI_NOT_EXISTS.getMessage(), null);
        }
        //check trade request for the trade id already exists
        TradeRequest tradeRequestCheck = tradeRequestRepository.findByTradeId(tradeRequestDto.getTradeId());
        if (tradeRequestCheck != null) {
            throw new ApiException(ErrorMessage.TRADE_ID_NOT_ALREADY_EXISTS.getCode(), ErrorMessage.TRADE_ID_NOT_ALREADY_EXISTS.getMessage(), null);
        }

        //save trade request
        TradeRequest tradeRequestDb = persistTradeRequest(tradeRequestDto);
        //create enriched market settlement message
        MarketSettlementMessageDto marketSettlementMessageDto = convertToMarketSettlementMessage(tradeRequestDb, ssiData);
        //log the generated new market settlement message
        persistSettlementMessage(marketSettlementMessageDto);

        return marketSettlementMessageDto;
    }

    public MarketSettlementMessageDto getMarketSettlementMessage(String tradeId) throws ApiException {
        log.info("Start get MarketSettlementMessage");

        //get trade request
        TradeRequest tradeRequestDb = tradeRequestRepository.findByTradeId(tradeId);
        if (tradeRequestDb == null) {
            throw new ApiException(ErrorMessage.TRADE_ID_NOT_EXISTS.getCode(), ErrorMessage.TRADE_ID_NOT_EXISTS.getMessage(), null);
        }
        //get SSI record
        SsiData ssiData = ssiDataRepository.findBySsiCode(tradeRequestDb.getSsiCode());

        return convertToMarketSettlementMessage(tradeRequestDb, ssiData);
    }

    private SettlementMessage persistSettlementMessage(MarketSettlementMessageDto marketSettlementMessageDto) {
        SettlementMessage settlementMessage = new SettlementMessage();
        settlementMessage.setMessage_id(marketSettlementMessageDto.getMessageId());
        settlementMessage.setMessage(marketSettlementMessageDto.toString());
        return settlementMessageRepository.save(settlementMessage);
    }

    private TradeRequest persistTradeRequest(TradeRequestDto tradeRequestDto) {
        TradeRequest tradeRequest = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequest);
        tradeRequest.setCreationTime(new Date());
        return tradeRequestRepository.save(tradeRequest);
    }

    private MarketSettlementMessageDto convertToMarketSettlementMessage(TradeRequest tradeRequest, SsiData ssiData) {
        PayerPartyDto payerPartyDto = PayerPartyDto.builder()
                .accountNumber(ssiData.getPayerAccNum())
                .bankCode(ssiData.getPayerBank())
                .build();

        ReceiverPartyDto receiverPartyDto = ReceiverPartyDto.builder()
                .accountNumber(ssiData.getReceiverAccNum())
                .bankCode(ssiData.getReceiverBank())
                .build();

        MarketSettlementMessageDto marketSettlementMessageDto = MarketSettlementMessageDto.builder()
                .tradeId(tradeRequest.getTradeId())
                .valueDate(tradeRequest.getValueDate())
                .currency(tradeRequest.getCurrency())
                .messageId(UUID.randomUUID().toString())
                .amount(new BigDecimal(tradeRequest.getAmount()))
                .supportingInformation(ssiData.getInfo())
                .payerParty(payerPartyDto)
                .receiverParty(receiverPartyDto)
                .build();

        return marketSettlementMessageDto;
    }
}
