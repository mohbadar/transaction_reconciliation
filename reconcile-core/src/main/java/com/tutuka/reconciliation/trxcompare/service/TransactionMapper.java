package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.trxcompare.util.DateUtil;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class TransactionMapper {

    public static Transaction map(TransactionWithScoreDTO dto)
    {
        Transaction transaction = new Transaction();
        transaction.setProfileName(dto.getProfileName1() == null ? dto.getProfileName2() : dto.getProfileName1());
        transaction.setTransactionAmount(dto.getTransactionAmount1() == null ? dto.getTransactionAmount2() : dto.getTransactionAmount1());
        transaction.setTransactionNarrative(dto.getTransactionNarrative1() == null ? dto.getTransactionNarrative2() : dto.getTransactionNarrative1());
        transaction.setTransactionDescription(dto.getTransactionDescription1() == null ? dto.getTransactionDescription2() : dto.getTransactionDescription1());
        transaction.setTransactionID(dto.getTransactionID1() == null ? dto.getTransactionID2() : dto.getTransactionID1());
        transaction.setTransactionType(dto.getTransactionType1() == 0 ? dto.getTransactionType2() : dto.getTransactionType1());
        transaction.setWalletReference(dto.getWalletReference1() == null ? dto.getWalletReference2() : dto.getWalletReference1());

        if(dto.getTransactionDate1() != null)
        {
            transaction.setTransactionDate(DateUtil.parseToLocalDateTime(dto.getTransactionDate1()));
        }else {
            transaction.setTransactionDate(DateUtil.parseToLocalDateTime(dto.getTransactionDate2()));
        }
        return transaction;
    }

}
