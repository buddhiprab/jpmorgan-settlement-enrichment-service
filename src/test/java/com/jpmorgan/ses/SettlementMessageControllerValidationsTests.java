package com.jpmorgan.ses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.enums.ErrorMessage;
import com.jpmorgan.ses.models.SsiData;
import com.jpmorgan.ses.models.TradeRequest;
import com.jpmorgan.ses.repositories.SsiDataRepository;
import com.jpmorgan.ses.repositories.TradeRequestRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*created by Buddhi*/

@SpringBootTest
@AutoConfigureMockMvc
class SettlementMessageControllerValidationsTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    SsiDataRepository ssiDataRepository;
    @MockBean
    TradeRequestRepository tradeRequestRepository;

    private static SsiData ssiData;
    private static TradeRequestDto tradeRequestDto;

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

    }

    @Test
    public void create_marketSettlementMessage_ssiCode_notExists_validation_400() throws Exception {

        String tradeRequest = "{\"tradeId\":\"16846548\", \"ssiCode\":\"OCBC_DBS_5\", \"amount\":12894.65, \"currency\":\"USD\", \"valueDate\":\"20022020\"}";

        mockMvc.perform(post("/market/settlement_message/create")
                .content(tradeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorMessage.SSI_NOT_EXISTS.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorMessage.SSI_NOT_EXISTS.getMessage()));
    }

    @Test
    public void create_marketSettlementMessage_tradeRequestId_alreadyExists_validation_400() throws Exception {
        when(ssiDataRepository.findBySsiCode(any(String.class))).thenReturn(ssiData);
        TradeRequest tradeRequestDb = new TradeRequest();
        copyProperties(tradeRequestDto, tradeRequestDb);
        when(tradeRequestRepository.findByTradeId(any(String.class))).thenReturn(tradeRequestDb);

        String tradeRequest = "{\"tradeId\":\"16846548\", \"ssiCode\":\"OCBC_DBS_2\", \"amount\":12894.65, \"currency\":\"USD\", \"valueDate\":\"20022020\"}";

        mockMvc.perform(post("/market/settlement_message/create")
                .content(tradeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorMessage.TRADE_ID_NOT_ALREADY_EXISTS.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorMessage.TRADE_ID_NOT_ALREADY_EXISTS.getMessage()));
    }

    @Test
    public void create_marketSettlementMessage_mandatory_validations_400() throws Exception {
        when(ssiDataRepository.findBySsiCode(any(String.class))).thenReturn(ssiData);
        when(tradeRequestRepository.findByTradeId(any(String.class))).thenReturn(null);

        String tradeRequest = "{}";

        mockMvc.perform(post("/market/settlement_message/create")
                .content(tradeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorMessage.VALIDATION_ERRORS.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorMessage.VALIDATION_ERRORS.getMessage()))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", hasSize(6)))
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"tradeId\" && @.errorDesc == \"ERR_API_VAL Field 'tradeId' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"ssiCode\" && @.errorDesc == \"ERR_API_VAL Field 'ssiCode' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"amount\" && @.errorDesc == \"ERR_API_VAL Field 'amount' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"amount\" && @.errorDesc == \"ERR_API_VAL Invalid format, value must be a number with optional 2 decimals\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"currency\" && @.errorDesc == \"ERR_API_VAL Field 'currency' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"valueDate\" && @.errorDesc == \"ERR_API_VAL Field 'valueDate' is mandatory\")]").exists());

    }

}
