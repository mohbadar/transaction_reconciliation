package com.tutuka.reconciliation.transactioncomapare.util;

import com.tutuka.reconciliation.transactioncomapare.enumeration.Result;
import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.transactioncomapare.domain.TransactionWithScoreDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
public class TransactionUtiltiy {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public boolean isValidDate(LocalDateTime date) {
		LocalDateTime now = LocalDateTime.now();
		if (now.compareTo(date) >= 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * Removes the duplicate transactions.<br>
	 * DUPLICATE - Transactions with same transactionID & Transaction
	 * 
	 * @return
	 **/
	public List<Transaction> removeDuplicates(List<Transaction> transactionList) {
		log.info("Removing Duplicate Transactions");
		Set<String> transactionIdSet = new HashSet<String>();
		List<Transaction> duplicateTransactions = new ArrayList<>();
		for (Iterator<Transaction> tutukaIterator = transactionList.iterator(); tutukaIterator.hasNext();) {
			Transaction transactionIter = tutukaIterator.next();
			String transactionId = transactionIter.getTransactionID() + "|"
					+ transactionIter.getTransactionDescription();
			if (transactionIdSet.contains(transactionId)) {
				duplicateTransactions.add(transactionIter);
				tutukaIterator.remove();
			}
			transactionIdSet.add(transactionId);
		}
		return duplicateTransactions;
	}

	/**
	 * Removes the BAD transactions.<br>
	 * BAD - Transactions with NULL (or ZERO)transactionID or Transaction
	 * 
	 **/
	public List<Transaction> removeBadTransactions(List<Transaction> transactionsList) {
		log.info("Removing NULL/BAD transactions");
		List<Transaction> badTransactionList = new ArrayList<>();
		for (Iterator<Transaction> tutukaIterator = transactionsList.iterator(); tutukaIterator.hasNext();) {
			Transaction transactionIter = tutukaIterator.next();
			if (transactionIter.getTransactionID() == null || transactionIter.getTransactionDescription() == null
					|| transactionIter.getTransactionID().equals(BigInteger.ZERO)) {
				log.warn("BAD Transaction found");
				badTransactionList.add(transactionIter);
				tutukaIterator.remove();
			}
		}
		return badTransactionList;
	}

	private void removeDuplicatesFileOne(List<Transaction> transactionList, List<TransactionWithScoreDTO> report,
			String fileName) {
		List<Transaction> FileOneDuplicates = new ArrayList<>();
		FileOneDuplicates = removeDuplicates(transactionList);
		for (Transaction fileOneDuplicate : FileOneDuplicates) {
			TransactionWithScoreDTO reportRecord = new TransactionWithScoreDTO();
			reportRecord.setFile1Name(fileName);
			reportRecord.setProfileName1(fileOneDuplicate.getProfileName());
			reportRecord.setTransactionAmount1(fileOneDuplicate.getTransactionAmount());
			reportRecord.setTransactionDate1(fileOneDuplicate.getTransactionDate().format(formatter));
			reportRecord.setTransactionDescription1(fileOneDuplicate.getTransactionDescription());
			reportRecord.setTransactionID1(fileOneDuplicate.getTransactionID());
			reportRecord.setTransactionNarrative1(fileOneDuplicate.getTransactionNarrative());
			reportRecord.setTransactionType1(fileOneDuplicate.getTransactionType());
			reportRecord.setWalletReference1(fileOneDuplicate.getWalletReference());
			reportRecord.setMatchScore(-1);
			reportRecord.setReasons(new StringBuilder("Duplicate transaction in File 1"));
			reportRecord.setStatus(Result.DUPLICATE);
			report.add(reportRecord);
		}
	}

	private void removeDuplicatesFileTwo(List<Transaction> transactionList, List<TransactionWithScoreDTO> report,
			String fileName) {
		List<Transaction> FileTwoDuplicates = new ArrayList<>();
		FileTwoDuplicates = removeDuplicates(transactionList);
		for (Transaction fileTwoDuplicate : FileTwoDuplicates) {
			TransactionWithScoreDTO reportRecord = new TransactionWithScoreDTO();
			reportRecord.setFile2Name(fileName);
			reportRecord.setProfileName2(fileTwoDuplicate.getProfileName());
			reportRecord.setTransactionAmount2(fileTwoDuplicate.getTransactionAmount());
			reportRecord.setTransactionDate2(fileTwoDuplicate.getTransactionDate().format(formatter));
			reportRecord.setTransactionDescription2(fileTwoDuplicate.getTransactionDescription());
			reportRecord.setTransactionID2(fileTwoDuplicate.getTransactionID());
			reportRecord.setTransactionNarrative2(fileTwoDuplicate.getTransactionNarrative());
			reportRecord.setTransactionType2(fileTwoDuplicate.getTransactionType());
			reportRecord.setWalletReference2(fileTwoDuplicate.getWalletReference());
			reportRecord.setMatchScore(-1);
			reportRecord.setReasons(new StringBuilder("Duplicate transaction in File 2"));
			reportRecord.setStatus(Result.DUPLICATE);
			report.add(reportRecord);
		}
	}

	private void removeBadTransactionsFileOne(List<Transaction> transactionList, List<TransactionWithScoreDTO> report,
			String fileName) {
		List<Transaction> FileOneDuplicates = new ArrayList<>();
		FileOneDuplicates = removeBadTransactions(transactionList);
		for (Transaction fileOneDuplicate : FileOneDuplicates) {
			TransactionWithScoreDTO reportRecord = new TransactionWithScoreDTO();
			reportRecord.setFile1Name(fileName);
			reportRecord.setProfileName1(fileOneDuplicate.getProfileName());
			reportRecord.setTransactionAmount1(fileOneDuplicate.getTransactionAmount());
			reportRecord.setTransactionDate1(fileOneDuplicate.getTransactionDate().format(formatter));
			reportRecord.setTransactionDescription1(fileOneDuplicate.getTransactionDescription());
			reportRecord.setTransactionID1(fileOneDuplicate.getTransactionID());
			reportRecord.setTransactionNarrative1(fileOneDuplicate.getTransactionNarrative());
			reportRecord.setTransactionType1(fileOneDuplicate.getTransactionType());
			reportRecord.setWalletReference1(fileOneDuplicate.getWalletReference());
			reportRecord.setMatchScore(-1);
			reportRecord.setReasons(new StringBuilder("NULL/Zero TransactionId/Transaction Narrative in File1"));
			reportRecord.setStatus(Result.BAD_TRANSACTION);
			report.add(reportRecord);
		}
	}

	private void removeBadTransactionsFileTwo(List<Transaction> transactionList, List<TransactionWithScoreDTO> report,
			String fileName) {
		List<Transaction> FileTwoDuplicates = new ArrayList<>();
		FileTwoDuplicates = removeBadTransactions(transactionList);
		for (Transaction fileTwoDuplicate : FileTwoDuplicates) {
			TransactionWithScoreDTO reportRecord = new TransactionWithScoreDTO();
			reportRecord.setFile2Name(fileName);
			reportRecord.setProfileName2(fileTwoDuplicate.getProfileName());
			reportRecord.setTransactionAmount2(fileTwoDuplicate.getTransactionAmount());
			reportRecord.setTransactionDate2(fileTwoDuplicate.getTransactionDate().format(formatter));
			reportRecord.setTransactionDescription2(fileTwoDuplicate.getTransactionDescription());
			reportRecord.setTransactionID2(fileTwoDuplicate.getTransactionID());
			reportRecord.setTransactionNarrative2(fileTwoDuplicate.getTransactionNarrative());
			reportRecord.setTransactionType2(fileTwoDuplicate.getTransactionType());
			reportRecord.setWalletReference2(fileTwoDuplicate.getWalletReference());
			reportRecord.setMatchScore(-1);
			reportRecord.setReasons(new StringBuilder("NULL/Zero TransactionId/Transaction Narrative in File2"));
			reportRecord.setStatus(Result.DUPLICATE);
			report.add(reportRecord);
		}
	}
	
	
	public void removeBadAndDuplicatesFileOne(List<Transaction> transactionList,
											  List<TransactionWithScoreDTO> report, String fileName) {
		removeBadTransactionsFileOne(transactionList, report, fileName);
		removeDuplicatesFileOne(transactionList, report, fileName);
	}

	public void removeBadAndDuplicatesFileTwo(List<Transaction> transactionList,
											  List<TransactionWithScoreDTO> report, String fileName) {
		removeBadTransactionsFileTwo(transactionList, report, fileName);
		removeDuplicatesFileTwo(transactionList, report, fileName);
	}


}
