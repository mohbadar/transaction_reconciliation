package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.domain.FileUploadDTO;
import com.tutuka.reconciliation.trxcompare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.trxcompare.enumeration.Result;
import com.tutuka.reconciliation.trxcompare.util.SimilarityMeasurementUtility;
import com.tutuka.reconciliation.trxcompare.util.TransactionSortComparator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SimilarityMeasurementService {

	@Value("${similarity.match_tolerance}")
	public double matchTolerance;

	@Value("${similarity.probable_match_tolerance}")
	public double probableMatchTolerance;

	@Value("${similarity.probable_mismatch_tolerance}")
	public double probableMismatchTolerance;

	@Value("${similarity.valid_increment}")
	public double validIncrement;

	@Value("${similarity.similarity_increment}")
	public double similarityIncrement;

	@Value("${similarity.transaction_narrative_valid_increment}")
	public double transactionNarrativeValidIncrement;

	@Value("${similarity.transaction_narrative_probable_match_increment}")
	public double transactionNarrativeProbableMatchIncrement;

	@Value("${similarity.transaction_narrative_probable_mismatch_increment}")
	public double transactionNarrativeProbableMismatchIncrement;

	@Value("${similarity.total_score}")
	public double totalScore;

	@Value("${similarity.permissible_match_lower}")
	public double permissibleMatchLower;

	@Value("${similarity.probable_match_lower}")
	public double probableMatchLower;

	@Value("${similarity.probable_mismatch_lower}")
	public double probableMismatchLower;

	@Autowired
	private PreprocessingService preprocessingService;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	SimilarityMeasurementUtility similarityMeasurementUtility = new SimilarityMeasurementUtility();


	/**
	 * Logic
	 *
	 * 1. get list of transactions for tutuka file and client file and also the files
	 * 2. Sort Transactions Based on TransactionID and TransactionDescription
	 * 2. Create a list of Data Transfer Object for TransactionWithScore
	 * 3. foreach transaction in tutuka file loop client transactions: do the following subtasks
	 * 			3.1 transactionID and transactionDescription in both transactions are the same, then set matched flag to true
	 * 				3.1.1 similarity measurement for walletReference
	 * 		    	3.1.2 transactionDate similarity measurement with Asia Timezone Difference(3hours = 180min)  in consideration
	 * 		    	3.1.3 similarity measurement for transaction
	 * 		    	3.1.4 similarity measure for transaction type
	 * 		    	3.1.5 similarity measurement for profileName
	 * 		    	3.1.5 similarity measurement for transactionNarrative with CousinSimilarity
	 * 		    	3.1.6 set values for Data Transfer Object
	 * 		        3.1.7 Calculate scoreBitSum of transaction
	 * 		        3.1.8 Categorize Transaction
	 * 		        3.1.6 add to list
	 * 4. If transaction in tutuka or client files are not matched add them to the list as UNMATCHED transaction
	 * 5. Return the list
	 * @param tutukaTransactionList
	 * @param clientTransactionList
	 * @param fileUploadDTO
	 * @return
	 */
	public List<TransactionWithScoreDTO> calculateSimilarityScoreWithFuzzyLoginMatch(List<Transaction> tutukaTransactionList,
																					 List<Transaction> clientTransactionList, FileUploadDTO fileUploadDTO) {

		//Sort Transactions Based on TransactionID and TransactionDescription
		Collections.sort(tutukaTransactionList, new TransactionSortComparator());
		Collections.sort(clientTransactionList, new TransactionSortComparator());

		//Create a list of Data Transfer Object for TransactionWithScore
		List<TransactionWithScoreDTO> reportList = new ArrayList<>();

		for (Transaction tutukaTx : tutukaTransactionList) {
			//foreach transaction in tutuka file loop client transactions: do the following subtasks
			for (Transaction clientTx : clientTransactionList) {

				TransactionWithScoreDTO txReport = new TransactionWithScoreDTO();
				if (!clientTx.isMatched()) {

					String clientSetIdentifier = clientTx.getTransactionID().toString() + "|"
							+ clientTx.getTransactionDescription();


					//The List is already sorted according to Transaction ID,Transaction Description
				   // transactionID and transactionDescription in both transactions are the same, then set matched flag to true
					if (tutukaTx.getTransactionID().compareTo(clientTx.getTransactionID()) <= 0
							&& !clientTx.isMatched()) {


						if (tutukaTx.getTransactionID().equals(clientTx.getTransactionID())) {
							if (tutukaTx.getTransactionDescription().equals(clientTx.getTransactionDescription())) {
								tutukaTx.setMatched(true);
								clientTx.setMatched(true);
								txReport = getSimilarityScoreOfTwoTransactions(tutukaTx,clientTx);
								txReport.setFile1Name(fileUploadDTO.getFileOne().getOriginalFilename());
								txReport.setFile2Name(fileUploadDTO.getFileTwo().getOriginalFilename());
								reportList.add(txReport);
							}
							break;
						}
					}
				}
			}

			//If transaction in tutuka or client files are not matched add them to the list as UNMATCHED transaction

			if (!tutukaTx.isMatched()) {
				TransactionWithScoreDTO unmatchedTutukaReportRecord = new TransactionWithScoreDTO();
				unmatchedTutukaReportRecord.setFile1Name(fileUploadDTO.getFileOne().getOriginalFilename());
				unmatchedTutukaReportRecord.setProfileName1(tutukaTx.getProfileName());
				unmatchedTutukaReportRecord.setTransactionAmount1(tutukaTx.getTransactionAmount());
				unmatchedTutukaReportRecord.setTransactionDate1(tutukaTx.getTransactionDate().format(formatter));
				unmatchedTutukaReportRecord.setTransactionDescription1(tutukaTx.getTransactionDescription());
				unmatchedTutukaReportRecord.setTransactionID1(tutukaTx.getTransactionID());
				unmatchedTutukaReportRecord.setTransactionNarrative1(tutukaTx.getTransactionNarrative());
				unmatchedTutukaReportRecord.setTransactionType1(tutukaTx.getTransactionType());
				unmatchedTutukaReportRecord.setWalletReference1(tutukaTx.getWalletReference());
				unmatchedTutukaReportRecord.setMatchScore(0);
				unmatchedTutukaReportRecord.setStatus(Result.UNMATCHED);
				unmatchedTutukaReportRecord.setReasons(new StringBuilder("Unmatched Transaction in File1"));
				reportList.add(unmatchedTutukaReportRecord);
			}

		}

		for (Transaction unMatchedClientTx : clientTransactionList) {
			TransactionWithScoreDTO unmatchedClientReportRecord = new TransactionWithScoreDTO();
			if (!unMatchedClientTx.isMatched()) {
				unmatchedClientReportRecord.setFile2Name(fileUploadDTO.getFileTwo().getOriginalFilename());
				unmatchedClientReportRecord.setProfileName2(unMatchedClientTx.getProfileName());
				unmatchedClientReportRecord.setTransactionAmount2(unMatchedClientTx.getTransactionAmount());
				unmatchedClientReportRecord
						.setTransactionDate2(unMatchedClientTx.getTransactionDate().format(formatter));
				unmatchedClientReportRecord.setTransactionDescription2(unMatchedClientTx.getTransactionDescription());
				unmatchedClientReportRecord.setTransactionID2(unMatchedClientTx.getTransactionID());
				unmatchedClientReportRecord.setTransactionNarrative2(unMatchedClientTx.getTransactionNarrative());
				unmatchedClientReportRecord.setTransactionType2(unMatchedClientTx.getTransactionType());
				unmatchedClientReportRecord.setWalletReference2(unMatchedClientTx.getWalletReference());
				unmatchedClientReportRecord.setMatchScore(0);
				unmatchedClientReportRecord.setStatus(Result.UNMATCHED);
				unmatchedClientReportRecord.setReasons(new StringBuilder("Unmatched Transaction in File2"));
				reportList.add(unmatchedClientReportRecord);
			}

		}

		return reportList;
	}

	/**
	 * Similarity Measurement for one transaction
	 */
	public List<Transaction> calculateSimilarTransaction(Transaction transaction, List<Transaction> transactionsList) throws IOException {
		System.out.println("Transaction List Size > "+ transactionsList.size());
		List<Transaction> similarTransactions = new ArrayList<>();

		//Sort Transactions Based on TransactionID and TransactionDescription
		Collections.sort(transactionsList, new TransactionSortComparator());
			//transactionID+transactionDescription+transactionNarrative+transactionProfile+walletReference
		transactionsList = preprocessingService.applyPreprocessingLogic(transactionsList);
		transaction = preprocessingService.applyPreprocessingLogic(Arrays.asList(transaction)).get(0);

		for (Transaction trx : transactionsList) {
			Double score = getSimilarityScoreOfTwoTransactions(transaction, trx).getMatchScore();

//			Double score = similarityMeasurementUtility.getSimilarityScore(transaction.getComparisonString(), trx.getComparisonString())*100;
			if(score > probableMatchLower)
			{
				System.out.println("Similary Score : "+score );
				trx.setSimilarityScore(score);
				similarTransactions.add(trx);
			}
		}

		return similarTransactions;
	}


	/**
	 * Caculate Similarity Matching Score of Transaction and Put Inside a TransactionWithScoreDTO Object
	 *
	 * 	1.similarity measurement for walletReference
	 * 	2.transactionDate similarity measurement with Asia Timezone Difference(3hours = 180min)  in consideration
	 * 	3.similarity measurement for transaction
	 * 	4.similarity measure for transaction type
	 * 	5.similarity measurement for profileName
	 * 	6.similarity measurement for transactionNarrative with CousinSimilarity
	 * 	7.set values for Data Transfer Object
	 * 	8.Calculate scoreBitSum of transaction
	 * 	9.Categorize Transaction
	 *
	 * @param transaction1
	 * @param transaction2
	 * @return
	 */
	public TransactionWithScoreDTO getSimilarityScoreOfTwoTransactions(Transaction transaction1, Transaction transaction2) {
		TransactionWithScoreDTO txReport = new TransactionWithScoreDTO();

		// similarity measurement for walletReference
		if (StringUtils.equals(transaction1.getWalletReference(), transaction2.getWalletReference())) {
			txReport.setMatchScore(txReport.getMatchScore() + validIncrement);
		} else {
			txReport.getReasons().append("| WalletReference Mismatch |");
		}


		// transactionDate similarity measurement with Asia Timezone Difference(3hours = 180min)  in consideration
		if ((transaction1.getTransactionDate() == null) ^ (transaction2.getTransactionDate() == null)) {
			txReport.getReasons().append("| TransactionDate Mismatch |");
		} else if (Objects.equals(transaction1.getTransactionDate(),
				transaction2.getTransactionDate())) {
			txReport.setMatchScore(txReport.getMatchScore() + validIncrement);
		} else if (Math.abs(
				Duration.between(transaction1.getTransactionDate(), transaction2.getTransactionDate())
						.toMinutes()) <= 180) {
			txReport.setMatchScore(txReport.getMatchScore() + similarityIncrement);
			txReport.getReasons().append("| TransactionDate Similar Match |");
		} else {
			txReport.getReasons().append("| TransactionDate Mismatch |");
		}

		// similarity measurement for transaction amount
		if ((transaction1.getTransactionAmount() == null) ^ (transaction2.getTransactionAmount() == null)) {
			txReport.getReasons().append("| TransactionAmount Mismatch |");
		} else if (Objects.equals(transaction1.getTransactionAmount(),
				transaction2.getTransactionAmount())) {
			txReport.setMatchScore(txReport.getMatchScore() + validIncrement);
		} else if (Math.abs(transaction1.getTransactionAmount()) == Math
				.abs(transaction2.getTransactionAmount())) {
			txReport.setMatchScore(txReport.getMatchScore() + similarityIncrement);
			txReport.getReasons().append("| TransactionAmount Fuzzy match |");
		} else {
			txReport.getReasons().append("| TransactionAmount Mismatch |");
		}

		// similarity measurement for transaction type
		if (transaction1.getTransactionType() == transaction2.getTransactionType()) {
			txReport.setMatchScore(txReport.getMatchScore() + validIncrement);
		} else {
			txReport.getReasons().append("| TransactionType Mismatch |");
		}

		// similarity measurement for transaction profileName
		if ((transaction1.getProfileName() == null) ^ (transaction2.getProfileName() == null)) {
			txReport.getReasons().append("| ProfileName Mismatch |");
		} else if (Objects.equals(transaction1.getProfileName(), transaction2.getProfileName())) {
			txReport.setMatchScore(txReport.getMatchScore() + validIncrement);
		} else {
			txReport.getReasons().append("| ProfileName Mismatch |");
		}

		// similarity measurement for transaction narrative
		if ((transaction1.getTransactionNarrative() == null)
				^ (transaction2.getTransactionNarrative() == null)) {
			txReport.getReasons().append("| TransactionNarrative Mismatch |");
		} else if (Objects.equals(transaction1.getTransactionNarrative(),
				transaction2.getTransactionNarrative())) {
			txReport.setMatchScore(txReport.getMatchScore() + transactionNarrativeValidIncrement);
		} else {

			double similarityScore = similarityMeasurementUtility.getSimilarityScore(
					transaction1.getTransactionNarrative(), transaction2.getTransactionNarrative());
			if (similarityScore > matchTolerance) {
				txReport.setMatchScore(txReport.getMatchScore() + transactionNarrativeValidIncrement);
			} else if (similarityScore > probableMatchTolerance) {
				txReport.setMatchScore(
						txReport.getMatchScore() + transactionNarrativeProbableMatchIncrement);
				txReport.getReasons().append("| TransactionNarrative Fuzzy Match |");
			} else if (similarityScore > probableMismatchTolerance) {
				txReport.setMatchScore(
						txReport.getMatchScore() + transactionNarrativeProbableMismatchIncrement);
				txReport.getReasons().append("| TransactionNarrative Fuzzy Mismatch |");
			} else {
				txReport.getReasons().append("| TransactionNarrative Mismatch |");
			}
		}

		//set values
//							txReport.setFile1Name(fileUploadDTO.getFileOne().getOriginalFilename());
		txReport.setProfileName1(transaction1.getProfileName());
		txReport.setTransactionAmount1(transaction1.getTransactionAmount());
		if (!(transaction1.getTransactionDate() == null)) {
			txReport.setTransactionDate1(transaction1.getTransactionDate().format(formatter));
		}
		txReport.setTransactionDescription1(transaction1.getTransactionDescription());
		txReport.setTransactionID1(transaction1.getTransactionID());
		txReport.setTransactionNarrative1(transaction1.getTransactionNarrative());
		txReport.setTransactionType1(transaction1.getTransactionType());
		txReport.setWalletReference1(transaction1.getWalletReference());
//							txReport.setFile2Name(fileUploadDTO.getFileTwo().getOriginalFilename());
		txReport.setProfileName2(transaction2.getProfileName());
		txReport.setTransactionAmount2(transaction2.getTransactionAmount());
		if (!(transaction2.getTransactionDate() == null)) {
			txReport.setTransactionDate2(transaction2.getTransactionDate().format(formatter));
		}
		txReport.setTransactionDescription2(transaction2.getTransactionDescription());
		txReport.setTransactionID2(transaction2.getTransactionID());
		txReport.setTransactionNarrative2(transaction2.getTransactionNarrative());
		txReport.setTransactionType2(transaction2.getTransactionType());
		txReport.setWalletReference2(transaction2.getWalletReference());

		Double score = txReport.getMatchScore();
		//calculate scoreBitSum
		int scoreBitSum = ((score == totalScore) ? 16 : 0)
				+ ((score >= permissibleMatchLower && score < totalScore)
				? 8
				: 0)
				+ ((score >= probableMatchLower
				&& score < permissibleMatchLower) ? 4 : 0)
				+ ((score >= probableMismatchLower
				&& score < probableMatchLower) ? 2 : 0)
				+ ((score >= 0.0 && score < probableMismatchLower) ? 1 : 0);

		// Categorize Matching Transactions
		switch (scoreBitSum) {
			case 0:
			case 1:
				txReport.setStatus(Result.PERFECT_MISMATCH);
				break;
			case 2:
				txReport.setStatus(Result.PROBABLE_MISMATCH);
				break;
			case 4:
				txReport.setStatus(Result.PROBABLE_MATCH);
				break;
			case 8:
				txReport.setStatus(Result.PERMISSIBLE_MATCH);
				break;
			case 16:
				txReport.setStatus(Result.PERFECT_MATCH);
				break;
		}


		return txReport;


	}
}