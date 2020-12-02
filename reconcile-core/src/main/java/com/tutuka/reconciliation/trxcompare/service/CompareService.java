package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.infrastructure.exception.TransactionWithScoreEmptyListException;
import com.tutuka.reconciliation.trxcompare.enumeration.Result;
import com.tutuka.reconciliation.trxcompare.util.FileUtility;
import com.tutuka.reconciliation.trxcompare.util.SimilarityMeasurementUtility;
import com.tutuka.reconciliation.trxcompare.util.TransactionUtiltiy;
import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.trxcompare.domain.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompareService {

    @Autowired
    private FileSystemStorageService storageService;

    @Autowired
    private SimilarityMeasurementService similarityMeasurementService;

    @Autowired
    private PreprocessingService preprocessingService;

    FileUtility fileUtil = new FileUtility();
    CsvReaderService csvBean = new CsvReaderService();

    private FileUploadDTO fileUploadDTO;


    public Map<String, Object> compareTransactions(FileUploadDTO fileLoader) throws IOException {
        List<TransactionWithScoreDTO> transactionsWithScores = new ArrayList<>();

        //store files
        storageService.store(fileLoader.getFileOne());
        storageService.store(fileLoader.getFileTwo());

        //Validates for Empty file, Invalid Headers and INVALID File extension
        fileUtil.validate(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString());
        fileUtil.validate(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());

        /**
         * Read the files into Transaction List
         * Apply pre-processing logics
         */
        List<Transaction> tutukaList = preprocessingService.applyPreprocessingLogic(csvBean.csvRead(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString()));
        List<Transaction> clientList = preprocessingService.applyPreprocessingLogic(csvBean.csvRead(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString()));

        //Remove duplicate and bad transactions from transaction list
        TransactionUtiltiy util = new TransactionUtiltiy();
        util.removeBadAndDuplicatesFileOne(tutukaList, transactionsWithScores, fileLoader.getFileOne().getOriginalFilename());
        util.removeBadAndDuplicatesFileTwo(clientList, transactionsWithScores, fileLoader.getFileTwo().getOriginalFilename());

        transactionsWithScores.addAll(similarityMeasurementService.calculateSimilarityScoreWithFuzzyLoginMatch(tutukaList, clientList, fileLoader));

        Map<String, Object> response = splitResultForFiltering(transactionsWithScores);
        response.put("status", HttpStatus.OK);

        this.fileUploadDTO = fileLoader;

        return response;
    }


    /**
     * Get A Transaction and find the similar transaction
     * @param dto
     * @return
     */
    public Map<String, Object> getSimilarTransaction(TransactionWithScoreDTO dto) throws IOException {
        Map<String, Object> response = new HashMap<>();

        //Select another for comparison. For Tutuka Transactions-> ClientFile, and for client Transactions -> Tutuka file
        String fileName = fileUploadDTO.getFileOne().getOriginalFilename();
        if(dto.getFile1Name() != null)
        {
            fileName = fileUploadDTO.getFileTwo().getOriginalFilename();
        }

        System.out.println("FileName: "+ fileName);

        List<Transaction> transactions = preprocessingService.applyPreprocessingLogic(csvBean.csvRead(storageService.load(fileName).toString()));

        if (transactions.size() < 1)
            throw new TransactionWithScoreEmptyListException("List of Transactions with score is null");

        Transaction transaction = TransactionMapper.map(dto);

        //get list of transactions from another file
        List<Transaction> similarTransactions = similarityMeasurementService.calculateSimilarTransaction(transaction, transactions);

        response.put("status", HttpStatus.OK);
        response.put("similarTransactions", similarTransactions);

        return response;
    }

    public List<Transaction> mapTransactionWithScoresToTransaction(List<TransactionWithScoreDTO> dtos)
    {
        List<Transaction> transactions = new ArrayList<>();
        dtos.forEach(transactionWithScoreDTO -> {
            transactions.add(TransactionMapper.map(transactionWithScoreDTO));
        });
        return transactions;
    }


    /**
     * Split List for sub-lists for rendering on UI
     * @param transactionWithScoreDTOS
     * @return
     */
    private Map<String, Object> splitResultForFiltering(List<TransactionWithScoreDTO> transactionWithScoreDTOS)
    {
        Map<String, Object> data = new HashMap<>();
        List<TransactionWithScoreDTO> badTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.BAD_TRANSACTION);

        List<TransactionWithScoreDTO> duplicateTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.DUPLICATE);
        List<TransactionWithScoreDTO> unmatchedTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.UNMATCHED);
        List<TransactionWithScoreDTO> perfectMatchTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.PERFECT_MATCH);
        List<TransactionWithScoreDTO> permissibleMatchTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.PERMISSIBLE_MATCH);
        List<TransactionWithScoreDTO> probableMismatchTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.PROBABLE_MISMATCH);
        List<TransactionWithScoreDTO> perfectMismatchTransactions = filterTransactionsWithScore(transactionWithScoreDTOS, Result.PERFECT_MISMATCH);

        data.put("duplicate", duplicateTransactions);
        data.put("bad", badTransactions);
        data.put("unmatched", unmatchedTransactions);
        data.put("perfectMatch", perfectMatchTransactions);
        data.put("permissibleMatch", permissibleMatchTransactions);
        data.put("probableMismatch", probableMismatchTransactions);
        data.put("perfectMismatch", perfectMatchTransactions);
        return data;
    }

    private List<TransactionWithScoreDTO> filterTransactionsWithScore(List<TransactionWithScoreDTO> dtos, Result result)
    {
        List<TransactionWithScoreDTO> filteredTransactions = new ArrayList<>();

        dtos.forEach(transactionWithScoreDTO -> {
//            System.out.println("Status> " +transactionWithScoreDTO.getStatus().name());
            if (transactionWithScoreDTO.getStatus().name().equalsIgnoreCase(result.getValue()))
            {
                filteredTransactions.add(transactionWithScoreDTO);
            }
        });
        return filteredTransactions;
    }


}
