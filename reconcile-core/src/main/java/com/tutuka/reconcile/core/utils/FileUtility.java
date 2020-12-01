package com.tutuka.reconcile.core.utils;

import com.tutuka.reconcile.core.infrastructure.exception.EmptyFileException;
import com.tutuka.reconcile.core.infrastructure.exception.FileExtensionException;
import com.tutuka.reconcile.core.infrastructure.exception.InvalidHeaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileUtility {
	public static final String SEPARATOR = System.getProperty("csv.separator", ",");
	public static final String HEAP_LOCATION = System.getProperty("heap.location", "/tmp/");
	public static final int TIME_OUT = Integer.parseInt(System.getProperty("time.out", "1"));
	public static final int MIN_THREAD_POOL = Integer.parseInt(System.getProperty("min.thread.pool", "2"));
	public static final int MAX_THREAD_POOL = Integer.parseInt(System.getProperty("max.thread.pool", "5"));
	public static final int MAX_QUEUE_CAPACITY = Integer.parseInt(System.getProperty("max.queue.capacity", "500"));
	public static final String KEY_FILE_ONE = "FILE_ONE";
	public static final String KEY_FILE_TWO = "FILE_TWO";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String[] validHeaderColumns= {"ProfileName", "TransactionDate", "TransactionAmount", "TransactionNarrative", "TransactionDescription", "TransactionID", "TransactionType", "WalletReference"};


	/**
	 * used to check null value
	 *
	 * @param object
	 * @return booean
	 */
	public static boolean isNull(Object object) {
		return object == null ? true : object.toString().length() == 0 ? true : false;
	}

	/**
	 * Convert Spring's multipart file to plain java.io.File Note that, here
	 * heap location is configuration based on the environment
	 *
	 * @param multipart
	 * @return File
	 * @throws IllegalStateException
	 * @throws java.io.IOException
	 */
	public static File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
		String destination = HEAP_LOCATION + multipart.getOriginalFilename();
		File convFile = new File(destination);
		multipart.transferTo(convFile);
		return convFile;
	}

	private void isValidFormat(String fileName) {
		if(!fileName.toLowerCase().endsWith(".csv")) {
			logger.error("The File " +fileName+ " is NOT a VALID CSV file!!");
			throw new FileExtensionException("The File " +fileName+ " is NOT a VALID CSV file!!");
		}
	}
	
	private void validateHeaders(String fileName)  {
		String header = null;
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			header = br.readLine();
			br.close();
		} catch(IOException e) {
			logger.error("IOException trying to validate headers of " +fileName);
			e.printStackTrace();
		}
	        if (header != null) {
	            String[] headerColumns = header.split(",");
	            Arrays.sort(validHeaderColumns);
	            Arrays.sort(headerColumns);
	            if(Arrays.asList(headerColumns).contains(Arrays.asList(validHeaderColumns))){
	            	logger.error("The File "+fileName+" contains INVALID or NO Headers.");
	            	throw new InvalidHeaderException("The File "+fileName+" contains INVALID or NO Headers.");
	            }
	        }
	        else {
	        	logger.error("The File " +fileName+ " is EMPTY or CORRUPT!!");
	        	throw new EmptyFileException("The File " +fileName+ " is EMPTY or CORRUPT!!");
	        }
	}
	
	public void validate(String fileName) {
		isValidFormat(fileName);
		validateHeaders(fileName);
	}
}