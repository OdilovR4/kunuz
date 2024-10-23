package kun.uz.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(fromAccount);
            MimeMessageHelper helper = null;
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // HTML formatini ko'rsatish uchun true qo'yiladi
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
