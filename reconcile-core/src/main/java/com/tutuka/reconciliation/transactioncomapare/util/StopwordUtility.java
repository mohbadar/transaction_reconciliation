package com.tutuka.reconciliation.transactioncomapare.util;

import ch.qos.logback.core.property.ResourceExistsPropertyDefiner;
import com.tutuka.reconciliation.infrastructure.internationalization.Translator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StopwordUtility {

    private static final String resourceFilePath = "englishStopWords.txt";
    private static ResourceLoader resourceLoader;


    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        this.resourceLoader= resourceLoader;
    }
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
     * Gets each line in file as string and convert to a list
     * @param resourceFilePath
     * @return
     * @throws IOException
     */
    private static List<String> getFileContentAsList(String resourceFilePath) throws IOException {

        ClassPathResource classPathResource = new ClassPathResource(resourceFilePath);

        InputStream inputStream = classPathResource.getInputStream();
        File file = File.createTempFile("test", ".txt");
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        if (!file.exists())
            throw new FileNotFoundException(Translator.toLocale("exception.file-not-found-exception"));

        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());

        return lines;

    }

}
