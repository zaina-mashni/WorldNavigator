package com.WorldNavigator.exceptions;

public class MissingStateInFactory extends RuntimeException {
    public MissingStateInFactory(String errorMessage) {
        super(errorMessage);
    }
}
