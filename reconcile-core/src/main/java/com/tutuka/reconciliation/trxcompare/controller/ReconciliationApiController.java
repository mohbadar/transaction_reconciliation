package com.tutuka.reconciliation.trxcompare.controller;

import com.tutuka.reconciliation.infrastructure.exception.EmptyFileException;
import com.tutuka.reconciliation.infrastructure.exception.FileExtensionException;
import com.tutuka.reconciliation.infrastructure.exception.InvalidHeaderException;
import com.tutuka.reconciliation.trxcompare.data.Transaction;
import com.tutuka.reconciliation.infrastructure.util.FileUtility;
import com.tutuka.reconciliation.infrastructure.util.TransactionUtiltiy;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tutuka.reconciliation.trxcompare.domain.FileUploadDTO;

@RestController
@RequestMapping("api")
public class ReconciliationApiController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FileSystemStorageService storageService;
    @Autowired
    private CompareService compareService;
    
    @Autowired
    private SimilarityMeasurementService fLogic;


    @PostMapping(value = "/process-files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE })
    public ResponseEntity<Map<String,Object>> processFile(
            @RequestParam(name = "info") String info,
            @RequestParam(name = "clientCsv", required = true) MultipartFile clientCsv,
            @RequestParam(name = "tutukaCsv", required = true) MultipartFile tutukaCsv
    ) {

        Map<String, Object> response = new HashMap<>();
        System.out.println("Processing Files........"+ info);



        FileUploadDTO fileLoader = new FileUploadDTO();
        fileLoader.setFileOne(tutukaCsv);
        fileLoader.setFileTwo(clientCsv);

        logger.info("Inside Post Method");
        long startTime1 = System.currentTimeMillis();
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
        System.out.println("TutukaTransactionsList>"+ tutukaList.toString());

        List<Transaction> clientList = csvBean.csvRead(storageService.load(fileLoader.getFileTwo().getOriginalFilename()).toString());

        System.out.println("ClientTransactionsList>"+ clientList.toString());
        //Remove the bad and duplicate transactions from the transaction List
        util.removeBadAndDuplicatesFileOne(tutukaList, report, fileLoader.getFileOne().getOriginalFilename());
        util.removeBadAndDuplicatesFileTwo(clientList, report, fileLoader.getFileTwo().getOriginalFilename());

        //Fuzzy Logic cross matching
        report.addAll(fLogic.fuzzyLogicMatch(tutukaList, clientList, fileLoader));
        logger.info("TotalTime = :" + (-startTime1 + (System.currentTimeMillis())));

        response.put("report", report);
        response.put("status", HttpStatus.OK);

        return  ResponseEntity.ok(response);
    }



    @PostMapping(value = "/compare-files", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE, MediaType.ALL_VALUE })
    public ResponseEntity<Map<String,Object>> checkFiles(
            @RequestParam(name = "info") String info,
            @RequestParam(name = "clientCsv", required = true) MultipartFile clientCsv,
            @RequestParam(name = "tutukaCsv", required = true) MultipartFile tutukaCsv
    ) {

        return  ResponseEntity.ok(compareService.compareTransactions(tutukaCsv, clientCsv));
    }


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
