package com.tutuka.reconcile.core.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class StorageException extends BaseUncheckedException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
