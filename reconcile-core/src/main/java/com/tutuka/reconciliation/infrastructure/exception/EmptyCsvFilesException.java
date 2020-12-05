package com.tutuka.reconciliation.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class EmptyCsvFilesException extends BaseUncheckedException {
    public EmptyCsvFilesException(String msg) {
        super(msg);
    }
}
