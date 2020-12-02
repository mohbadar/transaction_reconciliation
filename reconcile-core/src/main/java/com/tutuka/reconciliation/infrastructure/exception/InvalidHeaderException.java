package com.tutuka.reconciliation.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class InvalidHeaderException extends BaseUncheckedException {
    public InvalidHeaderException(String message) {
        super(message);
    }

    public InvalidHeaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
