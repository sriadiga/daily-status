package com.status.wrapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.velocity.VelocityEngineUtils;
import com.status.model.Status;
import com.status.service.StatusService;

public class EmailSender implements InitializingBean{
	
	@Autowired
	private  Properties emailCredentials;
	
	@Autowired
	private StatusService statusSvc;
	
	private VelocityEngine velocityEngine;
	
	private  static Logger logger = Logger.getLogger(EmailSender.class.getName());
	
	private Properties gmailSmtpProps;
	
	private Session session;
	
	private Message mimeMessage;
	
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		  this.velocityEngine = velocityEngine;
		 }
	
	void setup() {
	    try {
			gmailSmtpProps = new Properties();
			gmailSmtpProps.put("mail.smtp.starttls.enable", "true");
			gmailSmtpProps.put("mail.smtp.auth", "true");
			gmailSmtpProps.put("mail.smtp.host", "smtp.gmail.com");
			gmailSmtpProps.put("mail.smtp.port", "587");
			session = Session.getInstance(gmailSmtpProps, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(emailCredentials.getProperty("sender_mail"),
							   emailCredentials.getProperty("sender_password"));
				}
			});
			mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(emailCredentials.getProperty("sender_mail")));
			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailCredentials.getProperty("reciepient")));
			logger.info("sender cron setup is complete");
		} catch (Exception e) {
			logger.error("Some problem occured while setting up EmailSender:"
        			+ "cause : "+e.getMessage());
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setup();
	}
	
	void askForStatusMail() {
		try {
			logger.info("ask for status  cron is running");
			LocalDate localDate = LocalDate.now();
            mimeMessage.setSubject(emailCredentials.getProperty("sender_subject")+" "+
            DateTimeFormatter.ofPattern("dd/MM/yyy").format(localDate));
            String text = VelocityEngineUtils.mergeTemplateIntoString(
	        velocityEngine, emailCredentials.getProperty("sender_content"), "UTF-8", null);
            mimeMessage.setContent(text, "text/html; charset=utf-8");
            Transport.send(mimeMessage);
            logger.info("mail has been send asking for status");
        } catch (Exception e) {
        	logger.error("Some Problem occured while sending ask for status mail :"
        			+ "cause : "+e.getMessage());
        }
	}
	
	void sendSummaryMail(String subject,List<Status> statusList) {
		try {
			logger.info("Summary Mail  cron is running");
            mimeMessage.setSubject(subject);
            Map model = new HashMap();
            model.put("summary", statusList);
            String text = VelocityEngineUtils.mergeTemplateIntoString(
        	velocityEngine, emailCredentials.getProperty("summary_content"), "UTF-8", model);
            mimeMessage.setContent(text, "text/html; charset=utf-8");
            Transport.send(mimeMessage);
            logger.info("Satus Summary mail has been sent");
        } catch (MessagingException e) {
        	logger.error("Some Problem occured while sending status summary mail :"
        			+ "cause : "+e.getMessage());
        }
	}
	
	int createSummary() {
        Calendar c = new GregorianCalendar();
	    c.set(Calendar.HOUR_OF_DAY, 0);
	    c.set(Calendar.MINUTE, 0);
	    c.set(Calendar.SECOND, 0);
	    Date todaysDate = c.getTime();
		Date yesterdaysDate = DateUtils.addDays(todaysDate, -1);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		List<Status> statusList =statusSvc.getStatus(yesterdaysDate);
		if (statusList.isEmpty()) {
			logger.info("summary mail not sent beacuse no status were found");
			return 1;
		}
		sendSummaryMail(emailCredentials.getProperty("summary_subject")+" "+dateFormat.format(yesterdaysDate)
		, statusSvc.getStatus(yesterdaysDate));
		logger.info("Successfully sent the summary mail");
		return 0;
	}
}
