package com.tutuka.reconciliation.trxcompare.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tutuka.reconciliation.infrastructure.exception.EmptyFileException;
import com.tutuka.reconciliation.infrastructure.exception.FileExtensionException;
import com.tutuka.reconciliation.infrastructure.exception.InvalidHeaderException;
import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.trxcompare.util.FileUtility;
import com.tutuka.reconciliation.trxcompare.util.TransactionUtiltiy;
import com.tutuka.reconciliation.trxcompare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.trxcompare.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigInteger;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tutuka.reconciliation.trxcompare.domain.FileUploadDTO;

@RestController
@RequestMapping("api")
public class ReconciliationApiController {

    @Autowired
    private CompareService compareService;


    @PostMapping(value = "/compare-files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE })
    public ResponseEntity<Map<String,Object>> compareFiles(
            @RequestParam(name = "info") String info,
            @RequestParam(name = "clientCsv", required = true) MultipartFile clientCsv,
            @RequestParam(name = "tutukaCsv", required = true) MultipartFile tutukaCsv
    ) throws IOException {

        FileUploadDTO fileLoader = new FileUploadDTO();
        fileLoader.setFileOne(tutukaCsv);
        fileLoader.setFileTwo(clientCsv);

        return  ResponseEntity.ok(compareService.compareTransactions(fileLoader));
    }

    @PostMapping(value = "/similar-transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getSimilarTransactions(@RequestBody String data) throws IOException {
        TransactionWithScoreDTO dto = new TransactionWithScoreDTO();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(data);

        String file1Name = root.get("file1Name").asText().trim();
        String file2Name = root.get("file2Name").asText().trim();

        if(!file1Name.equalsIgnoreCase("null"))
        {
            dto.setProfileName1(root.get("profileName1").asText());
            dto.setFile1Name(file1Name);
            dto.setTransactionDate1(root.get("transactionDate1").asText());
            dto.setTransactionAmount1(root.get("transactionAmount1").asLong());
            dto.setTransactionNarrative1(root.get("transactionNarrative1").asText());
            dto.setTransactionDescription1(root.get("transactionDescription1").asText());

            if(!root.get("transactionID1").asText().equals("null"))
            {
                dto.setTransactionID1(new BigInteger(root.get("transactionID1").asText()));
            }
            dto.setTransactionType1(root.get("transactionType1").asInt());
            dto.setWalletReference1(root.get("walletReference1").asText());

        }

        if(!file2Name.equalsIgnoreCase("null"))
        {
            dto.setProfileName2(root.get("profileName2").asText());
            dto.setFile2Name(root.get("file2Name").asText());
            dto.setTransactionDate2(root.get("transactionDate2").asText());
            dto.setTransactionAmount2(root.get("transactionAmount2").asLong());
            dto.setTransactionNarrative2(root.get("transactionNarrative2").asText());
            dto.setTransactionDescription2(root.get("transactionDescription2").asText());

            if(!root.get("transactionID2").asText().equals("null")){
                dto.setTransactionID2(new BigInteger(root.get("transactionID2").asText()));
            }
            dto.setTransactionType2(root.get("transactionType2").asInt());
            dto.setWalletReference2(root.get("walletReference2").asText());

        }


        return ResponseEntity.ok(compareService.getSimilarTransaction(dto));
    }
    
}
