package com.tutuka.reconcile.core.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class FileExtensionException extends BaseUncheckedException {
    public FileExtensionException(String message) {
        super(message);
    }

    public FileExtensionException(String message, Throwable cause) {
        super(message, cause);
    }
}
