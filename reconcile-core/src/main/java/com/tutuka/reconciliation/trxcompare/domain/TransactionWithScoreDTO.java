package com.tutuka.reconciliation.trxcompare.domain;

import com.tutuka.reconciliation.trxcompare.enumeration.Result;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@ToString
public class TransactionWithScoreDTO {
	private String file1Name;
	private String profileName1;
	private String transactionDate1;
	private Long transactionAmount1;
	private String transactionNarrative1;
	private String transactionDescription1;
	private BigInteger transactionID1;
	private int transactionType1;
	private String walletReference1;
	
	private String file2Name;
	private String profileName2;
	private String transactionDate2;
	private Long transactionAmount2;
	private String transactionNarrative2;
	private String transactionDescription2;
	private BigInteger transactionID2;
	private int transactionType2;
	private String walletReference2;
	
	/*
	 * Used to flag unmatched transactions as well as to reduce the no of cross matching 
	 * attempts once matched with a transaction of another file
	*/ 
	private double matchScore;
	
	private StringBuilder reasons  = new StringBuilder("");
		
	private Enum<Result> status;
	

}