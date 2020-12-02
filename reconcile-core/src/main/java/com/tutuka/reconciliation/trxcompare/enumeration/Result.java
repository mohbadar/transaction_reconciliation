package com.tutuka.reconciliation.trxcompare.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
	PERFECT_MATCH("PERFECT_MATCH"),
	PERMISSIBLE_MATCH("PERMISSIBLE_MATCH"),
	PROBABLE_MATCH("PROBABLE_MATCH"),
	PROBABLE_MISMATCH("PROBABLE_MISMATCH"),
	PERFECT_MISMATCH("PERFECT_MISMATCH"),
	BAD_TRANSACTION("BAD_TRANSACTION"),
	DUPLICATE("DUPLICATE"),
	UNMATCHED("UNMATCHED");

	private String value;
}
