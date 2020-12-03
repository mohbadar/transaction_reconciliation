package com.tutuka.lib.logger;

import org.springframework.web.servlet.NoHandlerFoundException;

public interface Logger {

    /**
     * Logs at Debug logging level
     *
     * @param sessionId   session id
     * @param idType      type of id
     * @param id          id value
     * @param description description of log
     */
    void debug(String sessionId, String idType, String id, String description);

    /**
     * Logs at Warn logging level
     *
     * @param sessionId   session id
     * @param idType      type of id
     * @param id          id value
     * @param description description of log
     */
    void warn(String sessionId, String idType, String id, String description);

    /**
     * Logs at Error logging level
     *
     * @param sessionId   session id
     * @param idType      type of id
     * @param id          id value
     * @param description description of log
     */
    void error(String sessionId, String idType, String id, String description);

    /**
     * Logs at Info logging level
     *
     * @param sessionId   session id
     * @param idType      type of id
     * @param id          id value
     * @param description description of log
     */
    void info(String sessionId, String idType, String id, String description);

    /**
     * Logs at Trace logging level
     *
     * @param sessionId   session id
     * @param idType      type of id
     * @param id          id value
     * @param description description of log
     */
    void trace(String sessionId, String idType, String id, String description);

    void error(String s, Exception ex);
}