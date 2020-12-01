package com.tutuka.reconcile.core.controller;

import com.tutuka.reconcile.core.data.Transaction;
import com.tutuka.reconcile.core.infrastructure.exception.*;
import com.tutuka.reconcile.core.infrastructure.util.FileUtility;
import com.tutuka.reconcile.core.infrastructure.util.TransactionUtiltiy;
import com.tutuka.reconcile.core.service.CsvReaderService;
import com.tutuka.reconcile.core.service.SimilarityMeasurementService;
import com.tutuka.reconcile.core.service.TransactionReportWithScore;
import com.tutuka.reconcile.core.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import com.tutuka.reconcile.core.domain.FileUploadDTO;

@RestController
@RequestMapping("api")
public class ReconciliationApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StorageService storageService;

    @Autowired
    public ReconciliationApiController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @Autowired
    private SimilarityMeasurementService fLogic;

    @GetMapping("/upload")
    public ModelAndView listUploadedFiles() throws IOException {
    	logger.info("Inside upload Get Method");
 /*       model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(ReconciliationApiController.class,
                        "serveFile", path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf("\\")+1)).build().toString())
                .collect(Collectors.toList()));*/
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("uploadForm");
	    mav.addObject("fileLoader", new FileUploadDTO());
	    return mav;
    }
    
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
    @PostMapping("/upload")
    public ModelAndView UploadAndCompare(@ModelAttribute("fileLoader") FileUploadDTO fileLoader, Model model,
                                         RedirectAttributes redirectAttributes) {
    	logger.info("Inside Post Method");
    	long startTime1 = System.currentTimeMillis();
    	ModelAndView modelAndView = new ModelAndView();
    	List<TransactionReportWithScore> report = new ArrayList<>();
    	TransactionUtiltiy util = new TransactionUtiltiy();
    	
        storageService.store(fileLoader.getFileOne());
        storageService.store(fileLoader.getFileTwo());
        
        //Validates for Empty file, INVALID Headers and INVALID File extension
        FileUtility fileUtil = new FileUtility();
        fileUtil.validate(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString());
        fileUtil.validate(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());
        
        //Read the files into Transaction List
        CsvReaderService csvBean = new CsvReaderService();
		List<Transaction> tutukaList = csvBean.csvRead(storageService.load(fileLoader.getFileOne().getOriginalFilename()).toString());
		List<Transaction> clientList = csvBean.csvRead(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());
		
		//Remove the bad and duplicate transactions from the transaction List
		util.removeBadAndDuplicatesFileOne(tutukaList, report, fileLoader.getFileOne().getOriginalFilename());
		util.removeBadAndDuplicatesFileTwo(clientList, report, fileLoader.getFileTwo().getOriginalFilename());
		
		//Fuzzy Logic cross matching 		
		report.addAll(fLogic.fuzzyLogicMatch(tutukaList, clientList, fileLoader));
		logger.info("TotalTime = :" + (-startTime1 + (System.currentTimeMillis())));
		
		modelAndView.addObject("report", report);
		modelAndView.setViewName("detailedReportPage");
		
		logger.info("Redirecting to report from post method");
		
		return modelAndView;
    }
    
    @ExceptionHandler({EmptyFileException.class, FileExtensionException.class, DateTimeParseException.class, InvalidHeaderException.class})
    public ModelAndView handleEmptyFileError(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errors", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
    
    @ExceptionHandler({Exception.class})
    public ModelAndView handleStorageError(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errors", ex.getMessage());
        mav.setViewName("error");
        return mav;
    }
    
}
