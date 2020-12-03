package com.tutuka.reconciliation.transactioncomapare.controller;

import com.tutuka.lib.api.annotation.ThrowsException;
import com.tutuka.lib.api.annotation.ThrowsExceptions;
import com.tutuka.lib.api.config.EnableApiFactory;
import com.tutuka.lib.api.handler.ResponseHandler;
import com.tutuka.lib.api.handler.exception.InternalServerProblemException;
import com.tutuka.lib.api.handler.exception.ResourceNotFoundException;
import com.tutuka.reconciliation.infrastructure.constant.ApplicationGenericConstants;
import com.tutuka.reconciliation.transactioncomapare.domain.TransactionWithScoreDTO;
import com.tutuka.reconciliation.transactioncomapare.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;
import com.tutuka.reconciliation.transactioncomapare.domain.FileUploadDTO;

@RestController
@RequestMapping("api")
@EnableApiFactory
@Api(basePath = ApplicationGenericConstants.REQUEST_MAPPING_COMPARISONS, value = ApplicationGenericConstants.REQUEST_MAPPING_COMPARISONS,
        description = ApplicationGenericConstants.API_DESC, produces = ApplicationGenericConstants.MULTIPART_JSON)
public class ReconciliationApiController extends ResponseHandler {

    @Autowired
    private CompareService compareService;

    // metadat annotations
    @ApiOperation(value = ApplicationGenericConstants.API_OPERATION_COMPARE_TRANSACTION_DESC, response = Map.class)
    @ApiResponses(value = {  @ApiResponse(code = 200, message =ApplicationGenericConstants.API_OPERATION_COMPARE_SUCCESS ),
            @ApiResponse(code = 500, message =ApplicationGenericConstants.API_OPERATION_COMPARE_FAIL )})
    @ThrowsExceptions(value = {
            @ThrowsException(status = HttpStatus.NOT_FOUND, exception = ResourceNotFoundException.class),
            @ThrowsException(status = HttpStatus.INTERNAL_SERVER_ERROR, exception = InternalServerProblemException.class) })
    //mapping annotation
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

    // metadat annotations
    @ApiOperation(value = "", response = Map.class)
    @ApiResponses(value = {  @ApiResponse(code = 200, message = ApplicationGenericConstants.API_OPERATION_SIMILARITY_MEASUREMENT_SUCCESS),
            @ApiResponse(code = 500, message = ApplicationGenericConstants.API_OPERATION_SIMILARITY_MEASUREMENT_FAIL)})
    @ThrowsExceptions(value = {
            @ThrowsException(status = HttpStatus.NOT_FOUND, exception = ResourceNotFoundException.class),
            @ThrowsException(status = HttpStatus.INTERNAL_SERVER_ERROR, exception = InternalServerProblemException.class) })
    //mapping annotation
    @PostMapping(value = "/similar-transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getSimilarTransactions(@RequestBody String data) throws IOException {
        TransactionWithScoreDTO dto = TransactionMapper.map(data);
        return ResponseEntity.ok(compareService.getSimilarTransaction(dto));
    }
    
}
