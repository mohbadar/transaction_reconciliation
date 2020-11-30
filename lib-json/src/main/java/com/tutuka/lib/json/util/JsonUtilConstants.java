package com.tutuka.lib.json.util;

/**
 * This enum contains all the error codes and messages of Jsonutil class
 */
public enum JsonUtilConstants {

	IO_EXCEPTION_ERROR_CODE("UTL-101", "File not found"),
	JSON_GENERATION_ERROR_CODE("UTL-102", "Json not generated successfully"),
	JSON_MAPPING_ERROR_CODE("UTL-103", "Json mapping Exception"),
	JSON_PARSE_ERROR_CODE("UTL-104", "Json not parsed successfully"),
	JSON_PROCESSING_EXCEPTION("UTL-105", "json not processed successfully");
	public final String errorCode;
	public final String errorMessage;

	JsonUtilConstants(String string1, String string2) {
		this.errorCode = string1;
		this.errorMessage = string2;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
