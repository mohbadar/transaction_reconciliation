package com.tutuka.reconciliation.transactioncomapare.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
public class Transaction {
	
	private String profileName;
	private LocalDateTime transactionDate;
	private Long transactionAmount;
	private String transactionNarrative;
	private String transactionDescription;
	private BigInteger transactionID;
	private int transactionType;
	private String walletReference;
	
	//Useful in reducing the Cartesian cross matching complexity [Still O(n^2) though]
	private boolean isMatched;
	private Double similarityScore;

	/**
	 * Caculate HashCode for Objects of class Transaction Record
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((profileName == null) ? 0 : profileName.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
		result = prime * result + ((transactionDate == null) ? 0 : transactionDate.hashCode());
		result = prime * result + ((transactionDescription == null) ? 0 : transactionDescription.hashCode());
		result = prime * result + ((transactionID == null) ? 0 : transactionID.hashCode());
		result = prime * result + ((transactionNarrative == null) ? 0 : transactionNarrative.hashCode());
		result = prime * result + transactionType;
		result = prime * result + ((walletReference == null) ? 0 : walletReference.hashCode());
		return result;
	}

}
