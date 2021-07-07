package com.congnghejava.webbanhang.services;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.congnghejava.webbanhang.models.Mail;

@Service
public class EmailSenderService {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	public void sendEmail(Mail mail, String templateName) throws MailException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		Context context = new Context();
		context.setVariables(mail.getProps());

		String html = templateEngine.process(templateName, context);

		helper.setTo(mail.getTo());
		helper.setFrom(mail.getFrom());
		helper.setSubject(mail.getSubject());
		helper.setText(html, true);

		mailSender.send(message);
	}

}
