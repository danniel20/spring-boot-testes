package com.securityteste.securityspringteste.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeValidator.class)
@Documented
public @interface FileType {

	Class<? extends Payload> [] payload() default{};
	Class<?>[] groups() default{};
	String message() default "Somente formatos PNG, JPG E JPEG são permitidos!";
}
