package com.securityteste.securityspringteste.service.mail;

public interface EmailService {

	public void sendMail(EmailDetails details, String template, Object obj);
	public void sendMailWithoutTemplate(EmailDetails details);
}
