package Helper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailMessanger implements IMessanger{
    private final String senderEmail;
    private String recipientEmail;
    final String username = " "; // to be added
    final String password = " "; // to be added

    public EmailMessanger(String senderEmail, String recipientEmail) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
    }

    public void addRecipientEmail(String recipientEmail) {
        this.recipientEmail = this.recipientEmail + "," + recipientEmail;
    }

    @Override
    public void sendMessage(String text) {
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
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)); // to be added an email addr
            message.setSubject("Testing Subject");
            message.setText("text");

            Transport.send(message);

            System.out.println(text);
            System.out.printf("Email sent from %s to %s", senderEmail, recipientEmail);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
