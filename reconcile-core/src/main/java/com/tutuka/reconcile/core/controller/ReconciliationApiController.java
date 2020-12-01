package com.tutuka.reconcile.core.controller;

import com.tutuka.lib.api.handler.ResponseHandler;
import com.tutuka.reconcile.core.service.CsvReaderService;
import com.tutuka.reconcile.core.service.FuzzyLogicService;
import com.tutuka.reconcile.core.service.TransactionReportWithScore;
import com.tutuka.reconcile.core.data.Transaction;
import com.tutuka.reconcile.core.storage.StorageService;
import com.tutuka.reconcile.core.domain.FileUploadDTO;
import com.tutuka.reconcile.core.infrastructure.util.FileUtility;
import com.tutuka.reconcile.core.infrastructure.util.TransactionUtiltiy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReconciliationApiController extends ResponseHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StorageService storageService;

    @Autowired
    public ReconciliationApiController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @Autowired
	private FuzzyLogicService fLogic;

    @GetMapping("/upload")
    public ModelAndView listUploadedFiles(Model model) throws IOException {
    	logger.info("Inside upload Get Method");
 /*       model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadCompareController.class,
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
    public ModelAndView UploadAndCompare(@ModelAttribute("fileLoader") FileUploadDTO fileUploadDTO) {
    	logger.info("Inside Post Method");
    	long startTime1 = System.currentTimeMillis();
    	ModelAndView modelAndView = new ModelAndView();
    	List<TransactionReportWithScore> report = new ArrayList<>();
    	TransactionUtiltiy util = new TransactionUtiltiy();
    	
        storageService.store(fileUploadDTO.getFileOne());
        storageService.store(fileUploadDTO.getFileTwo());
        
        //Validates for Empty file, INVALID Headers and INVALID File extension
        FileUtility fileUtil = new FileUtility();
        fileUtil.validate(storageService.load(fileUploadDTO.getFileOne().getOriginalFilename()).toString());
        fileUtil.validate(storageService.load(fileUploadDTO.getFileTwo().getOriginalFilename()).toString());
        
        //Read the files into Transaction List
        CsvReaderService csvBean = new CsvReaderService();
		List<Transaction> tutukaList = csvBean.csvRead(storageService.load(fileUploadDTO.getFileOne().getOriginalFilename()).toString());
		List<Transaction> clientList = csvBean.csvRead(storageService.load(fileUploadDTO.getFileTwo().getOriginalFilename()).toString());
		
		//Remove the bad and duplicate transactions from the transaction List
		util.removeBadAndDuplicatesFileOne(tutukaList, report, fileUploadDTO.getFileOne().getOriginalFilename());
		util.removeBadAndDuplicatesFileTwo(clientList, report, fileUploadDTO.getFileTwo().getOriginalFilename());
		
		//Fuzzy Logic cross matching 		
		report.addAll(fLogic.fuzzyLogicMatch(tutukaList, clientList, fileUploadDTO));
		logger.info("TotalTime = :" + (-startTime1 + (System.currentTimeMillis())));
		
		modelAndView.addObject("report", report);
		modelAndView.setViewName("detailedReportPage");
		
		logger.info("Redirecting to report from post method");
		
		return modelAndView;
    }
}
