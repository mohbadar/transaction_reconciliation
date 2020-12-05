package com.tutuka.reconciliation.infrastructure.constant;

public class ApplicationGenericConstants {


    // Application Name
    private static final String APP_NAME = " RECONCILE_V1";
//    :::::::::::::::::::: LOG FACTORY CONSTANTS ::::::::::::::::::::::::::::::::::
    public static  boolean  APPENDABLE =true;
    public static  String   APPENDER_NAME = "org.apache.log4j.RollingFileAppender";
    public static  String   APPENDER_FILE_NAME = "logs/reconcile.log";
    public static  String   APPENDER_FILE_NAME_PATTERN = "logs/reconcile-%d{yyyy-MM-dd-HH}-%i.log";
    public static  String   APPENDER_MAX_FILE_SIZE = "5MB";
    public static  String   APPENDER_TOTAL_CAPACITY = "50MB";
    public static  Integer  APPENDER_MAX_HISTORY  = 10;
    public static  boolean  APPENDER_IMMEDIATE_FLUSH = true;
    public static  boolean  APPENDER_PRUDENT = true;

    //MultiThreading Config Params
    public static final Integer CORE_POOLING_SIZE = 10;
    public static final Integer MAX_POOLING_SIZE = 10;
    public static final Integer QUEUE_CAPACITY = 1000;
    public static final String DEFAULT_PREFIX = "THREAD-SERVICE -> ";


    //API
    /** The name of the request parameter/part for client markoff files */
    public static final String CLIENT_MARKOFF_FILE = "clientMarkoffFile";

    /** The name of the request parameter/part for tutuka markoff files */
    public static final String TUTUKA_MARKOFF_FILE = "tutukaMarkoffFile";

    /** Mime type/content type for JSON */
    public static final String MULTIPART_JSON = "application/json";

    /** Mime type/content type HTTP header for CSV document submission requests */
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /** Used for controller request mapping */
    public static final String REQUEST_MAPPING_COMPARISONS = "api/rest/comparisons";

    public static final String API_DESC = "Provides a RESTful API to compare transactions and get similar transactions.";
    public static final String API_OPERATION_COMPARE_TRANSACTION_DESC = "receive client and tutuka csv files and compare the transactions inside the files";
    public static final String API_OPERATION_COMPARE_SUCCESS = "Successfully compared the transactions and return a list of them";
    public static final String API_OPERATION_COMPARE_FAIL = "An Error/Exception has been occured while compareing transactions";

    public static final String API_OPERATION_SIMILARITY_MEASUREMENT_DESC = "receive a transaction and compare with the transactions of another file and get similar transactions";
    public static final String API_OPERATION_SIMILARITY_MEASUREMENT_SUCCESS = "Successfully found the similar transactions and return a list of them";
    public static final String API_OPERATION_SIMILARITY_MEASUREMENT_FAIL = "An Error/Exception has been occured while similarity measurement transactions";




}
