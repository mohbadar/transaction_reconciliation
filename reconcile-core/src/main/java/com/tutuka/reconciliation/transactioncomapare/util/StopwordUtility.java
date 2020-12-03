package com.tutuka.reconciliation.transactioncomapare.util;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StopwordUtility {

    private static final String resourceFilePath = "classpath:englishStopWords.txt";


    /**
     * Logic:
     *
     * 1. get list of english stopwords from file
     * 2. Split Text to words
     * 3. remove all stopwords from the text
     * 4. Join text together as a sentence
     * @param textLines
     * @return
     * @throws IOException
     */
    public static String eliminateStopWords(List<String> textLines) throws IOException {
        List<String> englishStopWords = getFileContentAsList(resourceFilePath);
        List<String> textWords = splitLinesToWords(textLines);
        textWords.removeAll(englishStopWords);
        return  String.join(" ", textWords);
    }


    /**
     * Splite a list of strings to a collection of words
     * @param essayLines
     * @return
     */
    private static List<String> splitLinesToWords(List<String> essayLines) {

        List<String> essayWords = new ArrayList<>();

        for (String line : essayLines) {
            List<String> words = Arrays.asList(line.split(" "));
            essayWords.addAll(words);
        }

        return essayWords;
    }

    /**
     * Gets each line of a file as string in  a list
     * @param resourceFilePath
     * @return
     * @throws IOException
     */
    private static List<String> getFileContentAsList(String resourceFilePath) throws IOException {

        File file = ResourceUtils.getFile(resourceFilePath);
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());

        return lines;

    }

}
