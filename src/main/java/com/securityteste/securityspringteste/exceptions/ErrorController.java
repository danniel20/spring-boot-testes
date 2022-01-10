package com.securityteste.securityspringteste.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class ErrorController {

    private static Logger logger = LoggerFactory.getLogger(ErrorController.class);

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(MaxUploadSizeExceededException exc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) {
		ra.addFlashAttribute("alert", "Arquivo excedeu o limite total permitido para upload!");
		return "redirect:/usuarios/new";
		// return "redirect:"+ request.getHeader("referer");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public String handleAccessDeniedException(AccessDeniedException exc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) {
		ra.addFlashAttribute("alert", "Você não possui permissão de acesso!");
		return "redirect:/home";
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) {
		logger.error("Exception during execution request", exc);
		ra.addFlashAttribute("alert", "Erro ao realizar requisição!");
		return "redirect:"+ request.getHeader("referer");
	}

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model, HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) {
        logger.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        ra.addFlashAttribute("alert", errorMessage);
		return "redirect:"+ request.getHeader("referer");
    }

}
