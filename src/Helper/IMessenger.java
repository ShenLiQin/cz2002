package Helper;

public interface IMessenger {
    void sendMessage(String subject, String text);
    void addRecipientEmail(String recipientEmail);
}
