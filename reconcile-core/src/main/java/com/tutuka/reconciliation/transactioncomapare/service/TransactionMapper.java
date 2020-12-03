package com.tutuka.reconciliation.transactioncomapare.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.transactioncomapare.util.DateUtil;

import java.math.BigInteger;

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

    public static TransactionWithScoreDTO map(String data) throws JsonProcessingException {
        TransactionWithScoreDTO dto = new TransactionWithScoreDTO();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);

        String file1Name = root.get("file1Name").asText().trim();
        String file2Name = root.get("file2Name").asText().trim();

        if(!file1Name.equalsIgnoreCase("null"))
        {
            dto.setProfileName1(root.get("profileName1").asText());
            dto.setFile1Name(file1Name);
            dto.setTransactionDate1(root.get("transactionDate1").asText());
            dto.setTransactionAmount1(root.get("transactionAmount1").asLong());
            dto.setTransactionNarrative1(root.get("transactionNarrative1").asText());
            dto.setTransactionDescription1(root.get("transactionDescription1").asText());

            if(!root.get("transactionID1").asText().equals("null"))
            {
                dto.setTransactionID1(new BigInteger(root.get("transactionID1").asText()));
            }
            dto.setTransactionType1(root.get("transactionType1").asInt());
            dto.setWalletReference1(root.get("walletReference1").asText());

        }

        if(!file2Name.equalsIgnoreCase("null"))
        {
            dto.setProfileName2(root.get("profileName2").asText());
            dto.setFile2Name(root.get("file2Name").asText());
            dto.setTransactionDate2(root.get("transactionDate2").asText());
            dto.setTransactionAmount2(root.get("transactionAmount2").asLong());
            dto.setTransactionNarrative2(root.get("transactionNarrative2").asText());
            dto.setTransactionDescription2(root.get("transactionDescription2").asText());

            if(!root.get("transactionID2").asText().equals("null")){
                dto.setTransactionID2(new BigInteger(root.get("transactionID2").asText()));
            }
            dto.setTransactionType2(root.get("transactionType2").asInt());
            dto.setWalletReference2(root.get("walletReference2").asText());

        }
        return dto;

    }

}
