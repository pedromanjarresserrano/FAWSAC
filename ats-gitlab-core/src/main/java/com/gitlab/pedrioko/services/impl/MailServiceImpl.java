package com.gitlab.pedrioko.services.impl;

import com.gitlab.pedrioko.core.lang.AppParam;
import com.gitlab.pedrioko.core.lang.annotation.Email;
import com.gitlab.pedrioko.domain.EmailAccount;
import com.gitlab.pedrioko.services.CrudService;
import com.gitlab.pedrioko.services.MailService;
import com.gitlab.pedrioko.services.StorageService;
import com.querydsl.core.types.dsl.PathBuilder;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * The Class SmtpMailSender.
 */
@Component("smtpmailsender")
public class MailServiceImpl implements MailService {
    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);


    private JavaMailSender emailSender;
    private VelocityContext context;
    @Autowired
    private CrudService crudService;
    private EventQueue<Event> saveQueues;
    private EmailAccount account;

    @Override
    public void send(String subject, String body, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(account.getUsername());
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        if (emailSender == null)
            emailSender = createEmailSender();
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
            mimeMessageHelper.setFrom(account.getUsername());
            mimeMessageHelper.setTo(tos.toArray(new String[tos.size()]));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(swOut.toString(), true);
            mimeMessageHelper.setReplyTo(account.getUsername());
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

    public JavaMailSender createEmailSender() {
        JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
        emailSender.setHost(account.getHost());
        emailSender.setPort(Math.toIntExact(account.getPort()));
        emailSender.setUsername(account.getUsername());
        emailSender.setPassword(account.getPassword());
        emailSender.setProtocol(account.getProtocol());
        Properties mailProps = new Properties();
        mailProps.setProperty("mail.transport.protocol", account.getProtocol());
        mailProps.setProperty("mail.smtp.auth", String.valueOf(account.isSmtpauth()));
        mailProps.setProperty("mail.smtp.starttls.enable", String.valueOf(account.isStarttls()));
        mailProps.setProperty("mail.debug", "false");
        emailSender.setJavaMailProperties(mailProps);
        return emailSender;
    }

    @Override
    public EmailAccount getEmailAccount() {
        eventsListener();
        if (account == null) {
            PathBuilder<?> path = crudService.getPathBuilder(EmailAccount.class);
            account = (EmailAccount) crudService.query().from(path).fetchFirst();
        }
        return account;
    }

    @PostConstruct
    private void init() {
        getEmailAccount();
        if (account == null) {
            EmailAccount fetchFirst = new EmailAccount(0, "Default Account", "", "", "", "", true, true, true, 0);
            account = crudService.saveOrUpdate(fetchFirst);
            emailSender = createEmailSender();
        }
    }

    private void eventsListener() {
        if (saveQueues == null) {
            try {
                saveQueues = EventQueues.lookup("saveQueue", EventQueues.APPLICATION, true);
                saveQueues.subscribe(event -> {
                    if ("saveEmailAccount".equals(event.getName())) {
                        account = (EmailAccount) crudService.refresh(event.getData());
                        emailSender = createEmailSender();
                    }
                });
            } catch (Exception e) {
                saveQueues = null;
                LOGGER.error("saveEmailAccount Queues Error");
            }
        }
    }
}
