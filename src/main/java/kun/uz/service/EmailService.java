package kun.uz.service;

import kun.uz.dto.RegistrationDTO;
import kun.uz.entity.EmailHistoryEntity;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.repository.EmailHistoryRepository;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SendingService sendingService;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    public String createByEmail(RegistrationDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.md5(dto.getPassword()));
        entity.setVisible(Boolean.TRUE);
        entity.setStatus(ProfileStatus.IN_REGISTRATION);
        entity.setCreatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return sendToEmail(entity.getId(), dto.getUsername());
    }

    public String sendToEmail(Integer id, String email){
        String emailContent = "<html><body>" +
                "<p>Click below to confirm your registration:</p>" +
                "<a href=\"http://localhost:8080/auth/registration/confirm/" + id + "\" " +
                "style=\"display: inline-block; padding: 10px 20px; font-size: 16px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; border: 1px solid #007bff;\">Confirm</a>" +
                "</body></html>";
        String title = "Kecha Barca yevordiku Realni jigarr";
        // xozircha title ni yuborib turamiz chunki bizda contentnimiz url va button ostida
        if(sendingService.sendSimpleMessage(email, title, emailContent)){
            emailHistoryDto(email,title,LocalDateTime.now());
            return "Confirmation code was sent to "+email;
        }

        return "You can not send more than 3 email in one minutes ";

    }
    public void emailHistoryDto(String email, String title, LocalDateTime requestTime){
        EmailHistoryEntity entity = new EmailHistoryEntity();
        entity.setEmail(email);
        entity.setMessage(title);
        entity.setCreatedDate(requestTime);
        emailHistoryRepository.save(entity);
    }

    public String emailConfirm(Integer id, LocalDateTime clickTime) {
        ProfileEntity entity = profileRepository.findById(id).get();
        if(entity.getStatus()!= ProfileStatus.IN_REGISTRATION){
            return "Your status is not in registration";
        }

        List<EmailHistoryEntity> emailHistoryEntity = emailHistoryRepository.getLastSentEmail(entity.getUsername());
        LocalDateTime sentDate = emailHistoryEntity.get(0).getCreatedDate();

        if(clickTime.minusSeconds(20).isAfter(sentDate)){
            return "Time was over to confirm your registration";
        }

        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.updateSome(entity);

        return "Registration successfully completed";
    }
}
