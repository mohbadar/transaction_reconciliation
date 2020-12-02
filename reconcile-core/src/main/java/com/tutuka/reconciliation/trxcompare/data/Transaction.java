package com.tutuka.reconciliation.trxcompare.data;

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

	@JsonIgnore
	private String comparisonString;
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

	public String getComparisonString()
	{
		return String.format("%s%s",transactionDescription,transactionNarrative);
	}

	/**
	 * Check if two objects are matched
	 * @param other
	 * @return
	 */
	public boolean match(Transaction other) {

		if (this == other)
			return true;
		if (other == null)
			return false;

		/**
		 * If transactionId and TransactionDescription not null and their them same, then it returns true
		 *
		 */
		if (transactionID != null && transactionDescription != null) {
			if (other.transactionID != null && other.transactionDescription != null) {
				if (transactionID == other.transactionID
						&& transactionDescription.equalsIgnoreCase(other.transactionDescription)) {
					return true;
				}
			}
		}


		/**
		 * If transactionAmount and WalletReference not null and their them same, then it returns true
		 *
		 */
		if (transactionAmount != null && walletReference != null) {
			if (other.transactionAmount != null && other.walletReference != null) {
				if (transactionAmount == other.transactionAmount
						&& walletReference.equalsIgnoreCase(other.walletReference)) {
					return true;
				}
			}
		}


		/**
		 * if profileName in one file is null and in another file is not, than return false
		 * also if they are not them same in both files, then return false
		 */
		if (profileName == null) {
			if (other.profileName != null)
				return false;
		} else if (!profileName.equals(other.profileName) && !profileName.equalsIgnoreCase(other.profileName))
			return false;


		/**
		 * if transactionAmount in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;

		/**
		 * if transactionDate in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (transactionDate == null) {
			if (other.transactionDate != null)
				return false;
		} else if (!transactionDate.equals(other.transactionDate))
			return false;


		/**
		 * if transactionDescription in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (transactionDescription == null) {
			if (other.transactionDescription != null)
				return false;
		} else if (!transactionDescription.equals(other.transactionDescription)
				&& !transactionDescription.equalsIgnoreCase(other.transactionDescription))
			return false;

		/**
		 * if transactionId in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (transactionID == null) {
			if (other.transactionID != null)
				return false;
		} else if (transactionID != other.transactionID)
			return false;

		/**
		 * if transactionNarrative in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (transactionNarrative == null) {
			if (other.transactionNarrative != null)
				return false;
		} else if (!transactionNarrative.equals(other.transactionNarrative)
				&& !transactionNarrative.equalsIgnoreCase(other.transactionNarrative))
			return false;

		/**
		 * if transactionType in both files are not the same, then return false
		 */
		if (transactionType != other.transactionType)
			return false;

		/**
		 * if walletReference in one file is null and in another file is not null, than return false
		 *  also if they are not them same in both files, then return false
		 */
		if (walletReference == null) {
			if (other.walletReference != null)
				return false;
		} else if (!walletReference.equals(other.walletReference)
				&& !walletReference.equalsIgnoreCase(other.walletReference))
			return false;


		return true;

	}


}
