package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.util.DateUtil;
import com.tutuka.reconciliation.trxcompare.util.StopwordUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PreprocessingService {

    public List<Transaction> applyPreprocessingLogic(List<Transaction> transactions) throws IOException {

        // Remove Stopwords from TransactionNative
        List<Transaction> stopwordLessTransactions = removeStopwordsFromTransactionNative(transactions);
        return transactions;
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
