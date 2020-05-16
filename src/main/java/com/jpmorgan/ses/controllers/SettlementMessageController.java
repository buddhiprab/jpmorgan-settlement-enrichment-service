package com.jpmorgan.ses.controllers;

import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.exception.ApiException;
import com.jpmorgan.ses.dto.ErrorResponseDto;
import com.jpmorgan.ses.dto.FieldErrorDto;
import com.jpmorgan.ses.services.SettlementMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
@RequestMapping("/market/settlement_message")
public class SettlementMessageController {
    @Autowired
    SettlementMessageService settlementMessageService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TradeRequestDto tradeRequestDto){
        try{
            MarketSettlementMessageDto message = settlementMessageService.createNewMarketSettlementMessage(tradeRequestDto);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ApiException e){
            ErrorResponseDto errorResponse = createErrorResponse(e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity create(@PathVariable String tradeId){
        try{
            MarketSettlementMessageDto message = settlementMessageService.getMarketSettlementMessage(tradeId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ApiException e){
            ErrorResponseDto errorResponse = createErrorResponse(e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


    private ErrorResponseDto createErrorResponse(ApiException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setErrorCode(e.getCode());
        errorResponseDto.setErrorMessage(e.getMessage());
        if(e.getFieldErrors()!=null){
            List<FieldErrorDto> fieldErrorDtos = e.getFieldErrors().stream().map(o->{
                FieldErrorDto fieldErrorDto = new FieldErrorDto();
                copyProperties(o,fieldErrorDto);
                return fieldErrorDto;
            }).collect(Collectors.toList());
            errorResponseDto.setFieldErrors(fieldErrorDtos);
        }
        return errorResponseDto;
    }

}
