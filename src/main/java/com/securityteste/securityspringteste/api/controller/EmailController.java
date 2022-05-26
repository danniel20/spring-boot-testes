package com.securityteste.securityspringteste.api.controller;

import com.securityteste.securityspringteste.model.Papel;
import com.securityteste.securityspringteste.service.mail.EmailDetails;
import com.securityteste.securityspringteste.service.mail.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "apiEmailController")
@RequestMapping("/api/mail")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@GetMapping("/test-mail")
    public String testMail(){
		EmailDetails details = EmailDetails.builder()
			.recipient("teste@teste.com")
			.subject("Email de teste")
			.build();

		emailService.sendMail(details, "mail/test/teste-mail", Papel.builder().nome("Aluno").build());
        return "ok";
    }

	@GetMapping("/test-mail-without-template")
    public String testMailWithoutTemplate(){
		EmailDetails details = EmailDetails.builder()
			.recipient("teste@teste.com")
			.subject("Email de teste")
			.msgBody("Olá, esse é um email de teste! :)")
			.build();

		emailService.sendMailWithoutTemplate(details);
        return "ok";
    }
}
