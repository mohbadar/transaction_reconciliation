package com.tutuka.reconciliation.transactioncomapare.util;

import com.tutuka.reconciliation.infrastructure.internationalization.Translator;
import org.apache.commons.codec.language.bm.NameType;
import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.apache.commons.codec.language.bm.RuleType;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;


public class SimilarityMeasurementUtility {
	private ArrayList<Double> cosineSimArray = new ArrayList<Double>();

	private Double getIndividualCosineSimilarityScore(String s1, String s2) {
		Map<CharSequence, Integer> first = Arrays.stream(s1.split(""))
				.collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
		Map<CharSequence, Integer> second = Arrays.stream(s2.split(""))
				.collect(Collectors.toMap(c -> c, c -> 1, Integer::sum));
		return cosineSimilarity(first, second);
	}
	

	private Double cosineSimilarity(final Map<CharSequence, Integer> vectorOne,
			final Map<CharSequence, Integer> vectorTwo) {
		if (vectorOne == null || vectorTwo == null) {
			throw new IllegalArgumentException();
		}
		double cosineSimilarity;
		if (vectorOne != null && vectorTwo != null) {
			Set<CharSequence> intersection = getIntersection(vectorOne, vectorTwo);

			double dotProduct = dotProduct(vectorOne, vectorTwo, intersection);
			double A = 0.0;
			for (Integer value : vectorOne.values()) {
				A += Math.pow(value, 2);
			}
			double B = 0.0;
			for (final Integer value : vectorTwo.values()) {
				B += Math.pow(value, 2);
			}

			if (A <= 0.0 || B <= 0.0) {
				cosineSimilarity = 0.0;
			} else {
				cosineSimilarity = (double) (dotProduct / (double) (Math.sqrt(A) * Math.sqrt(B)));
			}
		} else {
			throw new IllegalArgumentException(Translator.toLocale("exception.illegal-argument"));
		}
		return cosineSimilarity;
	}

	private Set<CharSequence> getIntersection(Map<CharSequence, Integer> vectorOne,
			Map<CharSequence, Integer> vectorTwo) {
		Set<CharSequence> intersection = new HashSet<>(vectorOne.keySet());
		intersection.retainAll(vectorTwo.keySet());
		return intersection;
	}

	private double dotProduct(Map<CharSequence, Integer> vectorOne, Map<CharSequence, Integer> vectorTwo,
			Set<CharSequence> intersection) {
		long dotProduct = 0;
		for (CharSequence key : intersection) {
			dotProduct += vectorOne.get(key) * vectorTwo.get(key);
		}
		return dotProduct;
	}
	
	/**
	 * Removes the vowels and generates array of codes encoded by Beider Morse Algorithm <br>
	 * returns the average cosine similarity score between the two arrays
	 **/
	public Double getSimilarityScore(String tutukaString, String clientString) {

		// Remove Vowels
		String tutuka = tutukaString.replaceAll("[aeiouAEIOU]", "");
		String client = clientString.replaceAll("[aeiouAEIOU]", "");

		/**
		 * 		Converts words into potential phonetic representations.
		 * 		This is a two-stage process. Firstly, the word is converted into a phonetic representation
		 * 		that takes into account the likely source language. Next, this phonetic representation is converted
		 * 		into a pan-European 'average' representation,
		 * 		allowing comparison between different versions of essentially the same word from different languages.
		 * 		Generate Phonetic codes with Beider-Morse Encoder
		 */
		PhoneticEngine beider = new PhoneticEngine(NameType.GENERIC, RuleType.APPROX, true);
		String[] tutukaEncodeArray = beider.encode(tutuka).split("\\|");
		String[] clientEncodeArray = beider.encode(client).split("\\|");

		int encodingLength = NumberUtils.min(tutukaEncodeArray.length, clientEncodeArray.length);

		Double totalScore = 0.00;
		for (int i = 0; i < encodingLength; i++) {
			totalScore += getIndividualCosineSimilarityScore(tutukaEncodeArray[i], clientEncodeArray[i]);
			cosineSimArray.add(getIndividualCosineSimilarityScore(tutukaEncodeArray[i], clientEncodeArray[i]));
		}
		Double AvgScore = (Double) (totalScore / encodingLength);

		return AvgScore;
	}
}
