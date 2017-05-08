package com.status.wrapper;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.status.model.Status;
import com.status.service.StatusService;
import com.sun.mail.imap.IMAPFolder;

public class EmailScanner {
	
	@Autowired
	private  Properties emailCredentials;
	
	@Autowired
	private StatusService statusSvc;
	
	private  static Logger logger = Logger.getLogger(EmailScanner.class.getName());

	private IMAPFolder folder;
	
	private String expectedSubject;
	
	public void init() {
		String emailIdToScan=emailCredentials.getProperty("email_id_to_scan");
		String password=emailCredentials.getProperty("password");
		expectedSubject=emailCredentials.getProperty("subject_to_scan");
		Properties props = new Properties();
		props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.imap.socketFactory.fallback", "false");
		Session  session = Session.getDefaultInstance(props, null);
		try {
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com",emailIdToScan,password);
			folder = (IMAPFolder) store.getFolder("inbox");
			folder.open(Folder.READ_WRITE);
		} catch (Exception e) {
			logger.error("Email Scanner cron has encontered a problem to initialize itself :"
        			+ "Reason : "+e.getMessage());
		}
	}
	
	

	public void getUnreadMailsAndStore() {
	    try {
	    	init();
			Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			int mailCount=folder.getUnreadMessageCount();
			if(mailCount > 0) {
				logger.info("Email Scanner Cron has found "+folder.getUnreadMessageCount()+" Unread messages");
			}
			for (Message msg : messages) {
				String  emailSubject = msg.getSubject();
				Address[] from = msg.getFrom();
				String senderEmailId = from == null ? null : ((InternetAddress) from[0]).getAddress();
				if ( ((emailSubject).matches("(.*)"+expectedSubject+"(.*)")) &&
					   !senderEmailId.equalsIgnoreCase(emailCredentials.getProperty("email_id_to_scan")) ) {
					Status status = new Status();
					status.setEmail(senderEmailId);
					status.setDate(msg.getReceivedDate());		
					String emailBody=this.getTextFromMessage(msg);
					status.setStatus(emailBody.split("\\s(On).*\\wrote:")[0].trim());
					statusSvc.addStatus(status);
					logger.info("Status Mail Read and Stored in db.");
				}

			}
		} catch (Exception e) {
			logger.error("some problem occured Email Scanner was not able to read inbox"
					+ "Reason:"+e.getMessage());
		}
	}
	

	private String getTextFromMessage(Message message) throws Exception {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break;
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
}
