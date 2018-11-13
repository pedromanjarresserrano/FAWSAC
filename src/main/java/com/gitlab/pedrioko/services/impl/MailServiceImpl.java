package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.services.MailService;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * The Class SmtpMailSender.
 */
@Component("smtpmailsender")
@PropertySource(value = "classpath:application.properties")
public class MailServiceImpl implements MailService {
    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Value("#{'${mail.username}'}")
    private String from;

    @Autowired
    private JavaMailSender emailSender;
    private VelocityContext context;

    @Override
    public void send(String subject, String body, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }

    @Override
    public void send(String subject, String body, List<String> tos, Map<String, File> attachment,
                     Map<String, Object> vars) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        if (vars != null && !vars.isEmpty()) {
            VelocityContext ctxEnc = new VelocityContext(vars);
            context = new VelocityContext(ctxEnc);
        }
        StringWriter swOut = new StringWriter();
        Velocity.evaluate(context, swOut, "EMAILTEMPLATE", "<html>\r\n" + " \r\n" + "<head></head>\r\n" + " \r\n"
                + "<body>" + body + "</body>\r\n" + " \r\n" + "</html>");

        try {
            mimeMessageHelper = new MimeMessageHelper(message, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(tos.toArray(new String[tos.size()]));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(swOut.toString(), true);
            mimeMessageHelper.setReplyTo(from);
            if (attachment != null && !attachment.isEmpty())
                attachment.forEach((k, v) -> {
                    try {
                        mimeMessageHelper.addAttachment(k, v);
                    } catch (MessagingException e) {
                        LOGGER.error("ERROR on addAttachment(): ", e);
                    }
                });

            emailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("ERROR on send(): ", e);

        }

    }

}
