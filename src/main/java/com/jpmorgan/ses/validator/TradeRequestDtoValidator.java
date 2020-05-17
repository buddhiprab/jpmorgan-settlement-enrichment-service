package com.jpmorgan.ses.validator;

import com.jpmorgan.ses.dto.TradeRequestDto;

/*created by Buddhi*/

public class TradeRequestDtoValidator<T extends TradeRequestDto> extends ObjectValidator<T> {

    public TradeRequestDtoValidator(String objectName) {
        super(objectName);
    }

    public static <T extends TradeRequestDto> ObjectValidator<T> create(String objectName) {
        ObjectValidator<T> validator = new ObjectValidator<>("");
        validator.field("tradeId", TradeRequestDto::getTradeId).mandatory();
        validator.field("ssiCode", TradeRequestDto::getSsiCode).mandatory();
        validator.field("amount", TradeRequestDto::getAmount).mandatory();
        validator.field("amount", TradeRequestDto::getAmount).invalidFormat("\\d+(\\.\\d{1,2})?$");
        validator.field("currency", TradeRequestDto::getCurrency).mandatory();
        validator.field("valueDate", TradeRequestDto::getValueDate).mandatory();
        return validator;
    }
}
