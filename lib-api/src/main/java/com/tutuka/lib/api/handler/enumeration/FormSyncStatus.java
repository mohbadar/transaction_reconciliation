package com.tutuka.lib.api.handler.enumeration;


public enum FormSyncStatus {

    SUCCESSFUL("SUCCESSFUL"),
    FAILED("FAILED");

    private final String text;

    FormSyncStatus(final String text)
    {
        this.text =text;
    }

    @Override
    public String toString() {
        return text;
    }
}