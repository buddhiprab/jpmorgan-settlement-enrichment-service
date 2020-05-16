package com.jpmorgan.ses.validator;

public abstract class Validator<T> {
    public abstract ValidationResult validate(T target);
}
