package com.jpmorgan.ses.validator;

/*created by Buddhi*/

public abstract class Validator<T> {
    public abstract ValidationResult validate(T target);
}
