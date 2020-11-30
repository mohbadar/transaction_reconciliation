package com.tutuka.lib.api.handler.enumeration;


public enum ExceptionType {

    NoHandlerFoundException,
    Exception,
    HttpRequestMethodNotSupportedException,
    MethodArgumentNotValidException,
    MissingServletRequestParameterException,
    ConstraintViolationException,
    HttpMessageNotReadableException,
    HttpMediaTypeNotSupportedException,

    ASMISException,
    ASMISDataSourceException,
    ASMISResourceNotFoundException;

    ExceptionType(){

    }
}
