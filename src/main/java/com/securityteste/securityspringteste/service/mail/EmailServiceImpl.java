package com.securityteste.securityspringteste.service.mail;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${spring.mail.addresses.from}")
	private String sender;

	@Override
	public void sendMail(EmailDetails details, String template, Object obj) {

		try {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
				messageHelper.setFrom(sender);
				messageHelper.setTo(details.getRecipient());
				messageHelper.setSubject(details.getSubject());

				if(details.getAttachment() != null){
					FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
					messageHelper.addAttachment(file.getFilename(), file);
				}

				Context context = new Context();
				context.setVariable("obj", obj);

				messageHelper.setText(templateEngine.process(template + ".txt", context), templateEngine.process(template + ".html", context));
			};

			javaMailSender.send(messagePreparator);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void sendMailWithoutTemplate(EmailDetails details) {
		try {
			MimeMessagePreparator messagePreparator = mimeMessage -> {
				MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
				messageHelper.setFrom(sender);
				messageHelper.setTo(details.getRecipient());
				messageHelper.setSubject(details.getSubject());
				messageHelper.setText(details.getMsgBody());

				if(details.getAttachment() != null){
					FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
					messageHelper.addAttachment(file.getFilename(), file);
				}
			};

			javaMailSender.send(messagePreparator);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
