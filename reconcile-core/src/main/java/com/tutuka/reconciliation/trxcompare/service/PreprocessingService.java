package com.tutuka.reconciliation.trxcompare.service;

import com.tutuka.reconciliation.trxcompare.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PreprocessingService {
    private static final String resourceFilePath = "classpath:englishStopWords.txt";

    @PostConstruct
    public void init() throws ParseException {
        System.out.println("DateFormat > "+ DateUtil.convertToLocalDateTimeViaInstant(DateUtil.parseDate("1/12/2014 16:33")));
    }

    public String eliminateStopWords(List<String> essayLines) throws IOException {
             List<String> englishStopWords = getFileContentAsList(resourceFilePath);

            log.info("Stop words");
            log.info(englishStopWords.toString());

            List<String> essayWords = splitLinesToWords(essayLines);

            long wordCountBeforeRemovingStopWords = essayWords.size();

            essayWords.removeAll(englishStopWords);

            long wordCountAfterRemovingStopWords = essayWords.size();

            log.info("wordCountBeforeRemovingStopWords: " + wordCountBeforeRemovingStopWords);
            log.info("wordCountAfterRemovingStopWords: " + wordCountAfterRemovingStopWords);

            log.info("Essay after removing stop words: ");
            log.info(essayWords.toString());

            return  String.join(" ", essayWords);
        }


    /**
     * Splite a list of strings to a collection of words
     * @param essayLines
     * @return
     */
    private List<String> splitLinesToWords(List<String> essayLines) {

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
    private List<String> getFileContentAsList(String resourceFilePath) throws IOException {

        File file = ResourceUtils.getFile(resourceFilePath);
        List<String> lines = Files.readAllLines(file.toPath());
        lines = lines.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());

        return lines;

    }




}
