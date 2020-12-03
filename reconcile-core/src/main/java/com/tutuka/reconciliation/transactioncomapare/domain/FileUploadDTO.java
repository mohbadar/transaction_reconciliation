package com.tutuka.reconciliation.transactioncomapare.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class FileUploadDTO {

	private MultipartFile fileOne;
	private MultipartFile fileTwo;

}