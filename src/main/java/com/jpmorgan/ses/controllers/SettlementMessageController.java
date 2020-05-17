package com.jpmorgan.ses.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jpmorgan.ses.dto.ErrorResponseDto;
import com.jpmorgan.ses.dto.FieldErrorDto;
import com.jpmorgan.ses.dto.MarketSettlementMessageDto;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.enums.ErrorMessage;
import com.jpmorgan.ses.exception.ApiException;
import com.jpmorgan.ses.services.SettlementMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.beans.BeanUtils.copyProperties;

/*created by Buddhi*/

@Slf4j
@RestController
@RequestMapping("/market/settlement_message")
public class SettlementMessageController {
    @Autowired
    SettlementMessageService settlementMessageService;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TradeRequestDto tradeRequestDto) {
        try {
            MarketSettlementMessageDto message = settlementMessageService.createNewMarketSettlementMessage(tradeRequestDto);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ApiException e) {
            ErrorResponseDto errorResponse = createErrorResponse(e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity get(@PathVariable String tradeId) {
        try {
            MarketSettlementMessageDto message = settlementMessageService.getMarketSettlementMessage(tradeId);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (ApiException e) {
            ErrorResponseDto errorResponse = createErrorResponse(e);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }


    private ErrorResponseDto createErrorResponse(ApiException e) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setErrorCode(e.getCode());
        errorResponseDto.setErrorMessage(e.getMessage());
        if (e.getFieldErrors() != null) {
            List<FieldErrorDto> fieldErrorDtos = e.getFieldErrors().stream().map(o -> {
                FieldErrorDto fieldErrorDto = FieldErrorDto.builder().build();
                copyProperties(o, fieldErrorDto);
                return fieldErrorDto;
            }).collect(Collectors.toList());
            errorResponseDto.setFieldErrors(fieldErrorDtos);
        }
        return errorResponseDto;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorDesc = null;
        if (e.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ex = (InvalidFormatException) e.getCause();
            String path = ex.getPath().stream().map(i -> {
                if (i.getIndex() < 0) {
                    return i.getFieldName();
                } else {
                    return String.valueOf(i.getIndex());
                }
            }).reduce("", (i, j) -> {
                try {
                    int v = Integer.parseInt(j);
                    return i += ("[" + j + "]");
                } catch (NumberFormatException nfe) {
                    return i += (("".equals(i) ? "" : ".") + j);
                }
            });
            errorDesc = "Invalid format for field: " + path;
        } else if (e.getCause() instanceof JsonProcessingException) {
            errorDesc = "Invalid JSON content";
        } else {
            return handleException(e);
        }

        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(ErrorMessage.INVALID_REQUEST_JSON.getCode());
        response.setErrorMessage(errorDesc);
        log.error("Request JSON parsing exception: ", e);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handleException(Exception e) {
        ErrorResponseDto response = new ErrorResponseDto();
        response.setErrorCode(ErrorMessage.WE_CANT_PROCESS_REQUEST.getCode());
        response.setErrorMessage(ErrorMessage.WE_CANT_PROCESS_REQUEST.getMessage());
        log.error("Exception during request: ", e);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
