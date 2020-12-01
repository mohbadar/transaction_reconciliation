package com.tutuka.reconcile.core.infrastructure.util;

import com.tutuka.lib.logger.Logger;
import com.tutuka.lib.logger.appender.RollingFileAppender;
import com.tutuka.lib.logger.factory.Logfactory;
import com.tutuka.reconcile.core.infrastructure.constant.ApplicationGenericConstants;

public class LoggerFactory {

    private static final RollingFileAppender ROLLING_APPENDER = new RollingFileAppender();

    static {

        ROLLING_APPENDER.setAppend(ApplicationGenericConstants.APPENDABLE);
        ROLLING_APPENDER.setAppenderName(ApplicationGenericConstants.APPENDER_NAME);
        ROLLING_APPENDER.setFileName(ApplicationGenericConstants.APPENDER_FILE_NAME);
        ROLLING_APPENDER.setFileNamePattern(ApplicationGenericConstants.APPENDER_FILE_NAME_PATTERN);
        ROLLING_APPENDER.setMaxFileSize(ApplicationGenericConstants.APPENDER_MAX_FILE_SIZE);
        ROLLING_APPENDER.setTotalCap(ApplicationGenericConstants.APPENDER_TOTAL_CAPACITY);
        ROLLING_APPENDER.setMaxHistory(ApplicationGenericConstants.APPENDER_MAX_HISTORY);
        ROLLING_APPENDER.setImmediateFlush(ApplicationGenericConstants.APPENDER_IMMEDIATE_FLUSH);
        ROLLING_APPENDER.setPrudent(ApplicationGenericConstants.APPENDER_PRUDENT);
    }

    /**
     * Get Logger for specific class
     *
     * @param className
     *            {@code Class} required classs where logger to be implemented
     * @return Logger {@code Logger}
     */
    public static Logger getLogger(Class<?> className) {
        return Logfactory.getDefaultRollingFileLogger(ROLLING_APPENDER, className);
    }
}
