package com.tutuka.reconciliation.trxcompare.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileUploadDTO {

	private MultipartFile fileOne;
	private MultipartFile fileTwo;

}