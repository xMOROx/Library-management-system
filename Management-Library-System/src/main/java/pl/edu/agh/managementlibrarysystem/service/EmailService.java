package pl.edu.agh.managementlibrarysystem.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Properties;


@Service
@RequiredArgsConstructor
public class EmailService {


    private final Session session;
    static public String getTypicalText(String type, String fullName, String bookName){
        switch (type){
            case "Books are due" -> {
                return "Hello "+fullName+". You have not returned "+ bookName+ " on time! Please return it at your earliest convenience.";
            }
            case "Return date is approaching" -> {
                return "Hello "+fullName+". Your book permit for "+bookName+" is close to running out. Consider it a warning.";
            }
            case "New books are available"->{
                return "Hello "+ fullName+". New books have been added to our glorious library. Feel free to check them out!";
            }
            case "No recent activity" -> {
                return "Hello "+ fullName+". We've noticed You haven't read anything in a while! Maybe it's time to change it!";
            }
            default -> {
                return null;
            }
        }
    }
    public EmailService(){
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp-mail.outlook.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        this.session = Session.getDefaultInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("mlsTO1111@outlook.com", "hF*187HkYm=)DpQJ");
                    }
                });
    }


    public String send( String subject, String text,String emailReceiver){
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mlsTO1111@outlook.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailReceiver)
            );
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            return "email sent succesfully!";
        } catch (MessagingException e) {
            return "Error: "+e.toString();
        }
    }
}

