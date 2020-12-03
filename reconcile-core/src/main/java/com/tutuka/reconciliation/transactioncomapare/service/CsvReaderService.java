package com.tutuka.reconciliation.transactioncomapare.service;

import com.google.common.base.Splitter;
import com.tutuka.reconciliation.transactioncomapare.data.Transaction;
import com.tutuka.reconciliation.infrastructure.exception.EmptyFileException;
import com.tutuka.reconciliation.transactioncomapare.util.TransactionUtiltiy;
import com.tutuka.reconciliation.transactioncomapare.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.io.*;
import java.math.BigInteger;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class CsvReaderService {
	
	private int profileNameIndex;
	private int transactionDateIndex;
	private int transactionAmountIndex;
	private int transactionNarrativeIndex;
	private int transactionDescriptionIndex;
	private int transactionIDIndex;
	private int transactionTypeIndex;
	private int walletReferenceIndex;

	TransactionUtiltiy util = new TransactionUtiltiy();
	LocalDateTime transactionDate = null;
	Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
	Splitter splitter = Splitter.on(pattern);

	/**
	 * Reads the file into a List of tutuka.compareTransactions.Transaction Objects
	 * 
	 * @param fileName
	 *            FileName
	 * @return List of transactions after appropriate validations and trimming<br>
	 */
	public List<Transaction> csvRead(String fileName) {
		log.debug("Inside csvRead method");
		List<Transaction> TransactionsList = new ArrayList<Transaction>();
		long startTime = System.currentTimeMillis();
		try {
			InputStream inputFS = new FileInputStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			String line = null;
			String[] headers = null;
			line = br.readLine();
			headers = line.split(",");

			/*
			 * This is done to obtain the indexes of the headers. Further in the parsing
			 * process, this is used so that order of the columns doesn't matter
			 */
			profileNameIndex = Arrays.asList(headers).indexOf("ProfileName");
			transactionDateIndex = Arrays.asList(headers).indexOf("TransactionDate");
			transactionAmountIndex = Arrays.asList(headers).indexOf("TransactionAmount");
			transactionNarrativeIndex = Arrays.asList(headers).indexOf("TransactionNarrative");
			transactionDescriptionIndex = Arrays.asList(headers).indexOf("TransactionDescription");
			transactionIDIndex = Arrays.asList(headers).indexOf("TransactionID");
			transactionTypeIndex = Arrays.asList(headers).indexOf("TransactionType");
			walletReferenceIndex = Arrays.asList(headers).indexOf("WalletReference");

			TransactionsList = br.lines().parallel().map(mapToItem).collect(Collectors.toList());
			br.close();
		} catch (FileNotFoundException e) {
			log.error("FileNotFoundException, The file " + fileName + " cannot be found ");
			e.printStackTrace();
		} catch (IOException e) {
			log.error("IOException from csvRead method when trying to read " + fileName + " into memory");
			e.printStackTrace();
		}

		if(TransactionsList.size()==0) {
			log.error("The file "+fileName+" has ZERO transactions");
			throw new EmptyFileException("The file "+fileName+" has ZERO transactions");
		}
		return TransactionsList;
	}

	private Function<String, Transaction> mapToItem = (line) -> {
		Transaction transaction = new Transaction();
		/*
		 * Splitter from Guava library takes care of fields that have commas within
		 * double quotes as well eg.) Address fields with commas in them
		 */
		String[] p = splitter.splitToList(line).toArray(new String[0]);

		if (p.length > profileNameIndex && profileNameIndex >= 0) {
			if (StringUtils.trimToNull(p[profileNameIndex]) != null) {
				transaction.setProfileName(p[profileNameIndex].replaceAll("[^a-zA-Z ]+", ""));
			}
		}

		if (p.length > transactionDateIndex && transactionDateIndex >= 0) {
			if (StringUtils.trimToNull(p[transactionDateIndex]) != null) {
				try {
					transactionDate = DateUtil.parseToLocalDateTime(p[transactionDateIndex]);
				} catch (Exception e) {
					transactionDate = DateUtil.convertToLocalDateTimeViaInstant(DateUtil.parseDate(p[transactionDateIndex]));
				}
				if (util.isValidDate(transactionDate)) {
					transaction.setTransactionDate(transactionDate);
				}
			}
		}

		if (p.length > transactionAmountIndex && transactionAmountIndex >= 0) {
			if (StringUtils.trimToNull(p[transactionAmountIndex]) != null) {
				transaction.setTransactionAmount(Long.valueOf(p[transactionAmountIndex].replaceAll("[^\\p{Digit}-]+", "")));
			}
		}

		if (p.length > transactionNarrativeIndex && transactionNarrativeIndex >= 0) {
			if (StringUtils.trimToNull(p[transactionNarrativeIndex]) != null) {
				transaction.setTransactionNarrative(
						Normalizer.normalize(p[transactionNarrativeIndex], Normalizer.Form.NFD).replaceAll("[^a-zA-Z0-9 ]+", ""));
			}
		}

		if (p.length > transactionDescriptionIndex && transactionDescriptionIndex >= 0) {
			if (StringUtils.trimToNull(p[4]) != null) {
				transaction.setTransactionDescription(p[transactionDescriptionIndex].replaceAll("[^a-zA-Z]+", ""));
			}
		}

		if (p.length > transactionIDIndex && transactionIDIndex >= 0) {
			if (StringUtils.trimToNull(p[transactionIDIndex]) != null) {
				transaction.setTransactionID(new BigInteger(p[transactionIDIndex].replaceAll("[^\\p{Digit}]+", "")));
			}
		}

		if (p.length > transactionTypeIndex & transactionTypeIndex >= 0) {
			if (StringUtils.trimToNull(p[transactionTypeIndex]) != null) {
				transaction.setTransactionType(Integer.parseInt(p[transactionTypeIndex].replaceAll("[^\\p{Digit}]+", "")));
			}
		}

		if (p.length > walletReferenceIndex && walletReferenceIndex >= 0) {
			if (StringUtils.trimToNull(p[walletReferenceIndex]) != null) {
				transaction.setWalletReference(p[walletReferenceIndex].replaceAll("[^a-zA-Z0-9 _]+", ""));
			}
		}

		return transaction;

	};
}
