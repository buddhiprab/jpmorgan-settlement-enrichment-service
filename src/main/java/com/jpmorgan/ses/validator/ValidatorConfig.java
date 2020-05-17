package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.dto.TradeRequestDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*created by Buddhi*/

@Configuration
public class ValidatorConfig {
    @Bean
    public Validator<TradeRequestDto> tradeRequestDtoValidatorConfig() {
        ObjectValidator<TradeRequestDto> tradeRequestDtoObjectValidator = TradeRequestDtoValidator.create("");

        return new Validator<TradeRequestDto>() {
            @Override
            public ValidationResult validate(TradeRequestDto target) {
                ValidationResult result = ValidationResult.initial();
                tradeRequestDtoObjectValidator.validateObject(target, result);
                return result;
            }
        };

    }
}
