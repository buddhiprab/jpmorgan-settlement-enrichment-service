package com.jpmorgan.ses.services;

import com.jpmorgan.ses.enums.ErrorMessage;
import com.jpmorgan.ses.dto.TradeRequestDto;
import com.jpmorgan.ses.exception.ApiException;
import com.jpmorgan.ses.validator.ValidationResult;
import com.jpmorgan.ses.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ValidationService {
    @Autowired
    Validator<TradeRequestDto> tradeRequestDtoValidatorConfig;

    public void validate(TradeRequestDto request) throws ApiException {
        log.info("Validating TradeRequest");
        ValidationResult validationResult = tradeRequestDtoValidatorConfig.validate(request);

        if(validationResult.hasValidationErrors()){
            throw new ApiException(ErrorMessage.VALIDATION_ERRORS.getCode(), ErrorMessage.VALIDATION_ERRORS.getMessage(),validationResult.getFieldErrors());
        }
    }
}
