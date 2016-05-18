import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;


public class anomalydetector1 {
	ArrayList<String> anomalies = new ArrayList<String>();
	StringBuilder anomaly = new StringBuilder();
	
	public  detectAnomaliesAndEmail {
		SearchLogFiles();
		System.out.println(anomalies);
		sendEmailToAdministrator();
	}
	
	public static void SearchLogFiles(){
		File oldLogfile = new File("/Users/Arwen/Documents/Software Architecture/Project/logs3.txt");
		File newLogfile = new File("/Users/Arwen/Documents/Software Architecture/Project/logs4.txt");
		HashSet<String> oldLogHashMap = new HashSet<String>();
		Scanner scanNewLogFile, scanOldLogFile;
		try {
			scanOldLogFile = new Scanner(oldLogfile);
			scanNewLogFile = new Scanner(newLogfile);
			while(scanOldLogFile.hasNextLine()) {
					oldLogHashMap.add(scanOldLogFile.nextLine());
				}
			while(scanNewLogFile.hasNextLine()) {
				String logdata = scanNewLogFile.nextLine();
				if(!oldLogHashMap.contains(logdata))
						anomalies.add(logdata);
			}
			for(String a : anomalies){
				anomaly.append(a);
				anomaly.append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void sendEmailToAdministrator(){
		String str;
		final String username = "bellevue148@gmail.com";
		final String password = "";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("bellevue148@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("abirami@uw.edu"));
			message.addRecipient(RecipientType.CC, new InternetAddress("navalk@uw.edu"));
			message.addRecipient(RecipientType.CC, new InternetAddress("svasisht@uw.edu"));
			message.addRecipient(RecipientType.CC, new InternetAddress("swetha91@uw.edu"));
			message.setSubject("Alert: Anomaly Processes Found in the Client Machine");
			message.setText("Theses are the processes that were found to be running as Anomalies \n" + anomaly);
			
			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}  
    
	

}
