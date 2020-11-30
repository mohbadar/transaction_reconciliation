package com.tutuka.lib.logger.exception;


import com.tutuka.lib.lang.exception.common.BaseUncheckedException;

public class EmptyPatternException extends BaseUncheckedException {

    /**
     * Unique id for serialization
     */
    private static final long serialVersionUID = 105555532L;

    /**
     * @param errorCode    unique exception code
     * @param errorMessage exception message
     */
    public EmptyPatternException(String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

}