package com.jpmorgan.ses.validator;

/*created by Buddhi*/

@FunctionalInterface
public interface Validator<T> {
    ValidationResult validate(T target);
}
