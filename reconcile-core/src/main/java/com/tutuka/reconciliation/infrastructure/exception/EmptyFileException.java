package com.tutuka.reconciliation.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class EmptyFileException extends BaseUncheckedException {
    public EmptyFileException(String message) {
        super(message);
    }

    public EmptyFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
