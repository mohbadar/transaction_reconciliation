package com.tutuka.reconciliation.transactioncomapare.service;

import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.util.StopwordUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class PreprocessingService {

    public List<Transaction> applyPreprocessingLogic(List<Transaction> transactions) throws IOException {
        // Remove Stopwords from TransactionNative
        List<Transaction> stopwordLessTransactions = removeStopwordsFromTransactionNative(transactions);
        return stopwordLessTransactions;
    }

    private List<Transaction> removeStopwordsFromTransactionNative(List<Transaction> transactions) throws IOException {
        List<Transaction> newTransactions = new ArrayList<>();
        for (Transaction transaction: transactions)
        {
            String transactionNative = StopwordUtility.eliminateStopWords(Arrays.asList(transaction.getTransactionNarrative()));
            transaction.setTransactionNarrative(transactionNative);
            newTransactions.add(transaction);
        }
        return newTransactions;
    }

}
