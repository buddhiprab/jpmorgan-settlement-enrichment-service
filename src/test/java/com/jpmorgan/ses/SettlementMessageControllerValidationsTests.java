package com.jpmorgan.ses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpmorgan.ses.enums.ErrorMessage;
import com.jpmorgan.ses.services.SettlementMessageService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SettlementMessageControllerValidationsTests {
    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SettlementMessageService settlementMessageService;

    @BeforeAll
    private static void init() {

    }

    @Test
    public void create_marketSettlementMessage_ssiCode_mandatory_validation_400() throws Exception {

        String tradeRequest = "{}";

        mockMvc.perform(post("/market/settlement_message/create")
                .content(tradeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorMessage.SSI_NOT_EXISTS.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorMessage.SSI_NOT_EXISTS.getMessage()));
    }

    @Test
    public void create_marketSettlementMessage_mandatory_validations_400() throws Exception {

        String tradeRequest = "{\"ssiCode\":\"OCBC_DBS_1\"}";

        mockMvc.perform(post("/market/settlement_message/create")
                .content(tradeRequest)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorMessage.VALIDATION_ERRORS.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorMessage.VALIDATION_ERRORS.getMessage()))
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors", hasSize(5)))
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"tradeId\" && @.errorDesc == \"ERR_API_VAL Field 'tradeId' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"amount\" && @.errorDesc == \"ERR_API_VAL Field 'amount' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"amount\" && @.errorDesc == \"ERR_API_VAL Invalid format, value must be a number with optional 2 decimals\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"currency\" && @.errorDesc == \"ERR_API_VAL Field 'currency' is mandatory\")]").exists())
                .andExpect(jsonPath("$.fieldErrors.[?(@.fieldCode == \"valueDate\" && @.errorDesc == \"ERR_API_VAL Field 'valueDate' is mandatory\")]").exists());

    }

}
