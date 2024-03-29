package com.tutuka.lib.api.handler;


import com.tutuka.lib.api.handler.enumeration.ExceptionType;
import com.tutuka.lib.api.handler.exception.InternalServerProblemException;
import com.tutuka.lib.api.handler.exception.ResourceNotFoundException;
import com.tutuka.lib.api.handler.model.ErrorResponseModel;
import com.tutuka.lib.api.handler.model.ResponseModel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@ControllerAdvice
@NoArgsConstructor
public class ResponseHandler {

    Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    /*
     * functions which wrap response object into adapter standard response.
     *
     * */
    public <T> ResponseEntity<ResponseModel<T>> responseStandardizer(T object, HttpStatus httpStatus) {
        ResponseModel<T> responseModel = new ResponseModel(object, httpStatus.value(), null);
        return new ResponseEntity(responseModel, httpStatus);
    }

    /*
     * functions for handling spring exceptions
     *
     * */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public ResponseModel<Object> NotFoundHandler(NoHandlerFoundException ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.NoHandlerFoundException, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors("No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL()));
        return new ResponseModel(null, HttpStatus.NOT_FOUND.value(), error);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public ResponseModel<Object> ExceptionHandler(Exception ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.Exception, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(ex.getMessage()));
        return new ResponseModel(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseModel<Object> HttpRequestMethodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
        log.error("Exception : ", ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request endpoint");
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.HttpRequestMethodNotSupportedException, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(builder.toString()));
        return new ResponseModel(null, HttpStatus.METHOD_NOT_ALLOWED.value(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseModel<Object> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        log.error("Exception : ", ex);
        ArrayList<String> errors = new ArrayList();
        Iterator var5 = ex.getBindingResult().getAllErrors().iterator();
        while (var5.hasNext()) {
            ObjectError error = (ObjectError) var5.next();
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.MethodArgumentNotValidException, null, ex.getMessage(), Instant.now().toEpochMilli(), errors);
        return new ResponseModel(null, HttpStatus.BAD_REQUEST.value(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseModel<Object> MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.MissingServletRequestParameterException, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(ex.getParameterName() + " parameter is missing"));
        return new ResponseModel(null, HttpStatus.BAD_REQUEST.value(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public ResponseModel<Object> ConstraintViolationExceptionCustomHandler(ConstraintViolationException ex) {
        log.error("Exception : ", ex);
        ArrayList<String> errors = new ArrayList();
        Iterator var5 = ex.getConstraintViolations().iterator();

        while (var5.hasNext()) {
            ConstraintViolation<?> violation = (ConstraintViolation) var5.next();
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.ConstraintViolationException, null, ex.getMessage(), Instant.now().toEpochMilli(), errors);
        return new ResponseModel(null, HttpStatus.BAD_REQUEST.value(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseModel<Object> HttpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.HttpMessageNotReadableException, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(ex.getMessage()));
        return new ResponseModel(null, HttpStatus.BAD_REQUEST.value(), error);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public ResponseModel<Object> HttpMediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException ex) {
        log.error("Exception : ", ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported");
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.HttpMediaTypeNotSupportedException, null, ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(builder.toString()));
        return new ResponseModel(null, HttpStatus.BAD_REQUEST.value(), error);
    }

    /*
     * functions for handling custom exceptions.
     *
     * */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    @ResponseBody
    public ResponseModel<Object> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.ASMISResourceNotFoundException, ex.getErrorCode(), ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(ex.getMessage()));
        return new ResponseModel(null, HttpStatus.NOT_FOUND.value(), error);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InternalServerProblemException.class})
    @ResponseBody
    public ResponseModel<Object> hibernateExceptionHandler(InternalServerProblemException ex) {
        log.error("Exception : ", ex);
        ErrorResponseModel error = new ErrorResponseModel(ExceptionType.ASMISResourceNotFoundException, ex.getErrorCode(), ex.getMessage(), Instant.now().toEpochMilli(), this.getErrors(ex.getMessage()));
        return new ResponseModel<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), error);
    }

    /*
     * Utility function specific to this class
     *
     * */
    private ArrayList<String> getErrors(String... errors) {
        ArrayList<String> errorMessages = new ArrayList();
        Collections.addAll(errorMessages, errors);
        return errorMessages;
    }

}
