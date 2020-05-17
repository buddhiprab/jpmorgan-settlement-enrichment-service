package com.jpmorgan.ses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.PayerPartyDto;
import com.jpmorgan.ses.dto.ReceiverPartyDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.models.TradeRequest;
import com.jpmorgan.ses.repositories.SettlementMessageRepository;
import com.jpmorgan.ses.repositories.SsiDataRepository;
import com.jpmorgan.ses.repositories.TradeRequestRepository;
import com.jpmorgan.ses.services.SettlementMessageService;
import com.jpmorgan.ses.services.ValidationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SettlementMessageServiceTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    SettlementMessageService settlementMessageService;
    @MockBean
    SsiDataRepository ssiDataRepository;
    @MockBean
    TradeRequestRepository tradeRequestRepository;
    @MockBean
    ValidationService validationService;
    @MockBean
    SettlementMessageRepository settlementMessageRepository;

    private static SsiData ssiData;
    private static TradeRequestDto tradeRequestDto;
    private static MarketSettlementMessageDto marketSettlementMessageDto;

    @BeforeAll
    private static void init() {
        ssiData = new SsiData();
        ssiData.setSsiCode("OCBC_DBS_1");
        ssiData.setPayerAccNum("438421");
        ssiData.setPayerBank("OCBCSGSGXXX");
        ssiData.setReceiverAccNum("05461368");
        ssiData.setReceiverBank("DBSSGB2LXXX");
        ssiData.setInfo("BNF:FFC-4697132");

        tradeRequestDto = TradeRequestDto.builder()
                .tradeId("16846548")
                .ssiCode("OCBC_DBS_1")
                .amount("12894.65")
                .currency("USD")
                .valueDate("20022020")
                .build();



        PayerPartyDto payerPartyDto = PayerPartyDto.builder()
                .accountNumber(ssiData.getPayerAccNum())
                .bankCode(ssiData.getPayerBank())
                .build();
        ReceiverPartyDto receiverPartyDto = ReceiverPartyDto.builder()
                .accountNumber(ssiData.getReceiverAccNum())
                .bankCode(ssiData.getReceiverBank())
                .build();

        marketSettlementMessageDto = MarketSettlementMessageDto.builder()
                .tradeId(tradeRequestDto.getTradeId())
                .messageId("cf871c04-b805-4acf-ae0e-da61d05a88e0")
                .amount(new BigDecimal(tradeRequestDto.getAmount()))
                .valueDate(tradeRequestDto.getValueDate())
                .currency(tradeRequestDto.getCurrency())
                .payerParty(payerPartyDto)
                .receiverParty(receiverPartyDto)
                .supportingInformation(ssiData.getInfo())
                .build();

    }

    @Test
    void createNewMarketSettlementMessage() throws Exception {
        when(ssiDataRepository.findBySsiCode(any(String.class))).thenReturn(ssiData);
        when(tradeRequestRepository.findByTradeId(any(String.class))).thenReturn(null);
        TradeRequest tradeRequest = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequest);
        when(tradeRequestRepository.save(any(TradeRequest.class))).thenReturn(tradeRequest);

        MarketSettlementMessageDto result = settlementMessageService.createNewMarketSettlementMessage(tradeRequestDto);

        assertEquals(tradeRequestDto.getTradeId(), result.getTradeId());
        assertEquals(tradeRequestDto.getValueDate(), result.getValueDate());
        assertEquals(tradeRequestDto.getCurrency(), result.getCurrency());
        assertEquals(tradeRequestDto.getAmount(), String.valueOf(result.getAmount()));
        assertEquals(ssiData.getPayerAccNum(), result.getPayerParty().getAccountNumber());
        assertEquals(ssiData.getPayerBank(), result.getPayerParty().getBankCode());
        assertEquals(ssiData.getReceiverAccNum(), result.getReceiverParty().getAccountNumber());
        assertEquals(ssiData.getReceiverBank(), result.getReceiverParty().getBankCode());
        assertEquals(ssiData.getInfo(), result.getSupportingInformation());
    }

    @Test
    void getMarketSettlementMessage() throws Exception {
        TradeRequest tradeRequest = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequest);
        when(tradeRequestRepository.findByTradeId(any(String.class))).thenReturn(tradeRequest);
        when(ssiDataRepository.findBySsiCode(any(String.class))).thenReturn(ssiData);

        MarketSettlementMessageDto result = settlementMessageService.getMarketSettlementMessage(tradeRequestDto.getTradeId());

        assertEquals(tradeRequestDto.getTradeId(), result.getTradeId());
        assertEquals(tradeRequestDto.getValueDate(), result.getValueDate());
        assertEquals(tradeRequestDto.getCurrency(), result.getCurrency());
        assertEquals(tradeRequestDto.getAmount(), String.valueOf(result.getAmount()));
        assertEquals(ssiData.getPayerAccNum(), result.getPayerParty().getAccountNumber());
        assertEquals(ssiData.getPayerBank(), result.getPayerParty().getBankCode());
        assertEquals(ssiData.getReceiverAccNum(), result.getReceiverParty().getAccountNumber());
        assertEquals(ssiData.getReceiverBank(), result.getReceiverParty().getBankCode());
        assertEquals(ssiData.getInfo(), result.getSupportingInformation());
    }
}
