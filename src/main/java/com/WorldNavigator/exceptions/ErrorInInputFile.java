package com.WorldNavigator.exceptions;

public class ErrorInInputFile extends RuntimeException {
    public ErrorInInputFile(String errorMessage) {
        super(errorMessage);
    }
}
