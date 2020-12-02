package com.tutuka.reconciliation.trxcompare.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDetailsDTO {

    private  int totalRecords;
    private  int matchingRecords;
    private  int unmatchingRecords;
    private  int duplicateRecords;

}
