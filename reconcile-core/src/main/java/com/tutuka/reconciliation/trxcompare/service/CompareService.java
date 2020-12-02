package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.infrastructure.util.FileUtility;
import com.tutuka.reconciliation.infrastructure.util.TransactionUtiltiy;
import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.domain.FileUploadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class CompareService {

    @Autowired
    private FileSystemStorageService storageService;
    @Autowired
    private SimilarityMeasurementService similarityMeasurementService;

    public Map<String, Object> compareTransactions(MultipartFile tutukaCsv, MultipartFile clientCsv)
    {
        Map<String, Object> response = new HashMap<>();

        Set<Transaction> tutukaUnmatchTransactions = new HashSet<>();
        Set<Transaction> clientUnmatchTransactions = new HashSet<>();

        FileUploadDTO fileLoader = new FileUploadDTO();
        fileLoader.setFileOne(tutukaCsv);
        fileLoader.setFileTwo(clientCsv);


        storageService.store(fileLoader.getFileOne());
        storageService.store(fileLoader.getFileTwo());

        //Validates for Empty file, INVALID Headers and INVALID File extension
        FileUtility fileUtil = new FileUtility();
        fileUtil.validate(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString());
        fileUtil.validate(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());

        //Read the files into Transaction List
        CsvReaderService csvBean = new CsvReaderService();
        List<Transaction> tutukaList = csvBean.csvRead(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString());
        System.out.println("TutukaTransactionsList>"+ tutukaList.toString());

        List<Transaction> clientList = csvBean.csvRead(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());

        System.out.println("ClientTransactionsList>"+ clientList.toString());

//        for (Transaction tutukaTransaction: tutukaList){
//            for (Transaction clientTransaction: clientList)
//            {
//                if (!tutukaTransaction.match(clientTransaction)){
//                    tutukaUnmatchTransactions.add(tutukaTransaction);
//                }
//            }
//        }
//
//        for (Transaction clientTransaction: clientList){
//            for (Transaction tutukaTransaction: tutukaList)
//            {
//                if (!tutukaTransaction.match(clientTransaction)){
//                    clientUnmatchTransactions.add(tutukaTransaction);
//                }
//            }
//        }

        List<TransactionReportWithScore> report = new ArrayList<>();
        TransactionUtiltiy util = new TransactionUtiltiy();

        //Remove the bad and duplicate transactions from the transaction List
        util.removeBadAndDuplicatesFileOne(tutukaList, report, fileLoader.getFileOne().getOriginalFilename());
        util.removeBadAndDuplicatesFileTwo(clientList, report, fileLoader.getFileTwo().getOriginalFilename());

        report.addAll(similarityMeasurementService.fuzzyLogicMatch(tutukaList, clientList, fileLoader));


//        response.put("report", report);
        response.put("status", HttpStatus.OK);
        response.put("tutukaUnmatched", report);
//        response.put("clientUnmatched", clientUnmatchTransactions);
        return response;
    }


    /**
     * Check if two objects are matched
     * @param t2
     * @param t1
     * @return
     */
    public boolean match(Transaction t1, Transaction t2) {

        if (t1 == t2)
            return true;
        if (t1 == null || t2 == null)
            return false;

        /**
         * If transactionId and TransactionDescription not null and their them same, then it returns true
         *
         */
        if (t1.getTransactionID() != null && t1.getTransactionDescription() != null) {
            if (t2.getTransactionID() != null && t2.getTransactionDescription() != null) {
                if (t1.getTransactionID() == t2.getTransactionID()
                        && t1.getTransactionDescription().equalsIgnoreCase(t2.getTransactionDescription())) {
                    return true;
                }
            }
        }


        /**
         * If transactionAmount and WalletReference not null and their them same, then it returns true
         *
         */
        if (t1.getTransactionAmount() != null && t1.getWalletReference() != null) {
            if (t2.getTransactionAmount() != null && t2.getWalletReference() != null) {
                if (t1.getTransactionAmount() == t2.getTransactionAmount()
                        && t1.getWalletReference().equalsIgnoreCase(t2.getWalletReference())) {
                    return true;
                }
            }
        }


        /**
         * if profileName in one file is null and in another file is not, than return false
         * also if they are not them same in both files, then return false
         */
        if (t1.getProfileName() == null) {
            if (t2.getProfileName() != null)
                return false;
        } else if (!t1.getProfileName().equals(t2.getProfileName()) && !t1.getProfileName().equalsIgnoreCase(t2.getProfileName()))
            return false;


        /**
         * if transactionAmount in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getTransactionAmount() == null) {
            if (t2.getTransactionAmount() != null)
                return false;
        } else if (!t1.getTransactionAmount().equals(t2.getTransactionAmount()))
            return false;

        /**
         * if transactionDate in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getTransactionDate() == null) {
            if (t2.getTransactionDate() != null)
                return false;
        } else if (!t1.getTransactionDate().equals(t2.getTransactionDate()))
            return false;


        /**
         * if transactionDescription in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getTransactionDescription() == null) {
            if (t2.getTransactionDescription() != null)
                return false;
        } else if (!t1.getTransactionDescription().equals(t2.getTransactionDescription())
                && !t1.getTransactionDescription().equalsIgnoreCase(t2.getTransactionDescription()))
            return false;

        /**
         * if transactionId in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getTransactionID() == null) {
            if (t2.getTransactionID() != null)
                return false;
        } else if (t1.getTransactionID() != t2.getTransactionID())
            return false;

        /**
         * if transactionNarrative in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getTransactionNarrative() == null) {
            if (t2.getTransactionNarrative() != null)
                return false;
        } else if (!t1.getTransactionNarrative().equals(t2.getTransactionNarrative())
                && !t1.getTransactionNarrative().equalsIgnoreCase(t2.getTransactionNarrative()))
            return false;

        /**
         * if transactionType in both files are not the same, then return false
         */
        if (t1.getTransactionType() != t2.getTransactionType())
            return false;

        /**
         * if walletReference in one file is null and in another file is not null, than return false
         *  also if they are not them same in both files, then return false
         */
        if (t1.getWalletReference() == null) {
            if (t2.getWalletReference() != null)
                return false;
        } else if (!t1.getWalletReference().equals(t2.getWalletReference())
                && !t1.getWalletReference().equalsIgnoreCase(t2.getWalletReference()))
            return false;


        return true;

    }

}
