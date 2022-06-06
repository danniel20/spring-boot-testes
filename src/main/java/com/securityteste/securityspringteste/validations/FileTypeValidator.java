package com.securityteste.securityspringteste.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile>{

	// @Override
	// public void initialize(FileType constraintAnnotation) {
	// }

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

		boolean result = true;

		if(multipartFile == null || multipartFile.isEmpty()){
			return true;
		}

		String contentType = multipartFile.getContentType();

		if(!isSupportedContentType(contentType)){
			result = false;
		}

		return result;
	}

	private boolean isSupportedContentType(String contentType){
		return contentType.equals("image/png")
			|| contentType.equals("image/jpeg")
			|| contentType.equals("image/jpg");
	}


}
