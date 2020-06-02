package pl.zpo.rdk.system.mailer;

public interface EmailSender {
    void sendEmail(String to, String subject, String content);
}