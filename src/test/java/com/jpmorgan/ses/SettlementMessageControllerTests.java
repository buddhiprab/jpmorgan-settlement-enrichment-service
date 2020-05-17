package com.jpmorgan.ses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.PayerPartyDto;
import com.jpmorgan.ses.dto.ReceiverPartyDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.services.SettlementMessageService;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*created by Buddhi*/

@SpringBootTest
@AutoConfigureMockMvc
class SettlementMessageControllerTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SettlementMessageService settlementMessageService;

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
    void create_marketSettlementMessage_OK() throws Exception {
        when(settlementMessageService.createNewMarketSettlementMessage(Mockito.any(TradeRequestDto.class))).thenReturn(marketSettlementMessageDto);

        mockMvc.perform(post("/market/settlement_message/create")
                .content(om.writeValueAsString(tradeRequestDto)).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tradeId").value(marketSettlementMessageDto.getTradeId()))
                .andExpect(jsonPath("$.messageId").value(marketSettlementMessageDto.getMessageId()))
                .andExpect(jsonPath("$.amount").value(marketSettlementMessageDto.getAmount()))
                .andExpect(jsonPath("$.valueDate").value(marketSettlementMessageDto.getValueDate()))
                .andExpect(jsonPath("$.currency").value(marketSettlementMessageDto.getCurrency()))
                .andExpect(jsonPath("$.payerParty.accountNumber").value(marketSettlementMessageDto.getPayerParty().getAccountNumber()))
                .andExpect(jsonPath("$.payerParty.bankCode").value(marketSettlementMessageDto.getPayerParty().getBankCode()))
                .andExpect(jsonPath("$.receiverParty.accountNumber").value(marketSettlementMessageDto.getReceiverParty().getAccountNumber()))
                .andExpect(jsonPath("$.receiverParty.bankCode").value(marketSettlementMessageDto.getReceiverParty().getBankCode()))
                .andExpect(jsonPath("$.supportingInformation").value(marketSettlementMessageDto.getSupportingInformation()));
    }

    @Test
    void get_marketSettlementMessage_OK() throws Exception {
        when(settlementMessageService.getMarketSettlementMessage(marketSettlementMessageDto.getTradeId())).thenReturn(marketSettlementMessageDto);

        mockMvc.perform(get("/market/settlement_message/"+marketSettlementMessageDto.getTradeId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tradeId").value(marketSettlementMessageDto.getTradeId()))
                .andExpect(jsonPath("$.messageId").value(marketSettlementMessageDto.getMessageId()))
                .andExpect(jsonPath("$.amount").value(marketSettlementMessageDto.getAmount()))
                .andExpect(jsonPath("$.valueDate").value(marketSettlementMessageDto.getValueDate()))
                .andExpect(jsonPath("$.currency").value(marketSettlementMessageDto.getCurrency()))
                .andExpect(jsonPath("$.payerParty.accountNumber").value(marketSettlementMessageDto.getPayerParty().getAccountNumber()))
                .andExpect(jsonPath("$.payerParty.bankCode").value(marketSettlementMessageDto.getPayerParty().getBankCode()))
                .andExpect(jsonPath("$.receiverParty.accountNumber").value(marketSettlementMessageDto.getReceiverParty().getAccountNumber()))
                .andExpect(jsonPath("$.receiverParty.bankCode").value(marketSettlementMessageDto.getReceiverParty().getBankCode()))
                .andExpect(jsonPath("$.supportingInformation").value(marketSettlementMessageDto.getSupportingInformation()));
    }

}
