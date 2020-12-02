package com.tutuka.reconciliation.infrastructure.exception;

import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class TransactionWithScoreEmptyListException extends BaseUncheckedException {

    public TransactionWithScoreEmptyListException(String message)
    {
        super(message);
    }
}
