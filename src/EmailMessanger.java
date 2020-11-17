public class EmailMessanger implements IMessanger{
    private final String senderEmail;
    private String recipientEmail;

    public EmailMessanger(String senderEmail, String recipientEmail) {
        this.senderEmail = senderEmail;
        this.recipientEmail = recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
        System.out.printf("Email sent from %s to %s", senderEmail, recipientEmail);
    }
}
