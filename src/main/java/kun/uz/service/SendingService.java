package kun.uz.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kun.uz.repository.EmailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SendingService {

    @Value("${spring.mail.username}")
    private String fromAccount;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    EmailHistoryRepository emailHistoryRepository;

    public Boolean sendSimpleMessage(String to, String subject, String text) {
        if(checkTime(to)){
            return false;
        }

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
        return true;
    }

    private Boolean checkTime(String to) {
        Long emailCount = emailHistoryRepository.getEmailCount(to, LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        if(emailCount >=3) {
            return true;
        }
        return false;
    }

}
