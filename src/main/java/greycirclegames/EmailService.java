package greycirclegames;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import greycirclegames.frontend.TemplateHandler;

public class EmailService {
	private static final String BASE_URL = "greycirclegames.com";
	private static final String USERNAME = "info@greycirclegames.com";
	private static final String PASSWORD = "Ek7cChbw";
	private static final String OUT_SMTP = "mail.greycirclegames.com";
	
	public static void sendNewFriendMail(String to, String friendUsername){
		sendMail(new Options(to, friendUsername + " has added you in Grey Circle Games.", friendUsername + " has added you in Grey Circle Games.", TemplateHandler.FRIENDS_ROUTE));
	}
	
	public static void sendNewGameMail(String to, String gameType, String url){
		sendMail(new Options(to, "A new " + gameType + " game has been started with you.", "A " + gameType + " game has been started.", url));
	}
	
	public static void sendTurnMail(String to, String gameType, String url){
		sendMail(new Options(to, "It is your turn.", "Everyone is waiting very patiently for you to make your cunning move.", url));
	}
	
    public static void sendSkippedTurnMail(String to, String gameType, String url){
        sendMail(new Options(to, "Your turn has been skipped.", "We tried our darndest, but we couldn't find you any valid moves! Your turn was skipped.", url));
    }
	
	public static void sendGameOverMail(String to, String url){
		sendMail(new Options(to, "A game has ended.", "Rematch?", url));
	}
	
	public static void sendPokeMail(String to, String url){
		sendMail(new Options(to, "Reminder - It is your turn!", "Someone else in the game has requested to remind you that it is your turn.", url));
	}
	
	public static void sendMail(Options mail){
		Properties properties = System.getProperties();
		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.host", OUT_SMTP);
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.auth", "true");
		Authenticator authenticator = new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(USERNAME, PASSWORD);
		    }
		};
		Session session = Session.getDefaultInstance(properties, authenticator);
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(USERNAME));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));
			message.setSubject(mail.getSubject());
			message.setContent(mail.getBody() + "<br \\><br \\><a href=\"" + BASE_URL + mail.getUrl() + "\">Click here to view the update.</a>", "text/html");
			if(!mail.getTo().equals("")){
				Transport transport = session.getTransport("smtp");
				transport.connect(USERNAME, PASSWORD);
				transport.sendMessage(message, message.getAllRecipients());
				transport.close();
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static class Options {
		private String to;
		private String subject;
		private String body;
		private String url;
		public Options(){
			
		}
		public Options(String to, String subject, String body, String url){
			this.to = to;
			this.subject = subject;
			this.body = body;
			this.url = url;
		}
		public String getTo() {
			return to;
		}
		public void setTo(String to) {
			this.to = to;
		}
		public String getSubject() {
			return subject;
		}
		public void setSubject(String subject) {
			this.subject = subject;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	}
}
