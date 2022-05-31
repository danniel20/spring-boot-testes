package com.securityteste.securityspringteste.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile>{

	private static final Integer MB = 1024 * 1024;

	private long maxSizeInMB;

	@Override
	public void initialize(FileSize constraintAnnotation) {
		this.maxSizeInMB = constraintAnnotation.maxSizeInMB();
	}

	@Override
	public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

		if(multipartFile == null){
			return true;
		}

		return multipartFile.getSize() < maxSizeInMB * MB;
	}


}
