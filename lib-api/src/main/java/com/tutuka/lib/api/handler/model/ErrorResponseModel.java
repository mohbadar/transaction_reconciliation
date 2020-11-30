package com.tutuka.lib.api.handler.model;

import com.tutuka.lib.api.handler.enumeration.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponseModel {

	private ExceptionType exceptionType;

    private String errorCode;

    private String errorMessage;

    private long time;

    private List<String> errorCause;
}
