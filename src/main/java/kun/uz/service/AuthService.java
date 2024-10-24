package kun.uz.service;

import kun.uz.dto.RegistrationDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    EmailSendingService emailSendingService;
    LocalDateTime requestTime;

    public String registration(RegistrationDTO dto) {
        ProfileEntity emailEntity = profileRepository.getByEmailAndVisibleTrue(dto.getEmail());
        ProfileEntity entity = new ProfileEntity();
        if(emailEntity != null) {
            if(emailEntity.getStatus() == ProfileStatus.IN_REGISTRATION) {
                return sendUrl(emailEntity.getId(),dto.getEmail());
            }
            return "This email already exist";
        }
        else {
            entity.setEmail(dto.getEmail());
            entity.setName(dto.getName());
            entity.setSurname(dto.getSurname());
            entity.setPassword(MD5Util.md5(dto.getPassword()));
            entity.setStatus(ProfileStatus.IN_REGISTRATION);
            entity.setCreatedDate(LocalDateTime.now());
            profileRepository.save(entity);
        }

       return sendUrl(entity.getId(),entity.getEmail());

    }

    public String sendUrl(Integer id, String email){
        String emailContent = "<html><body>" +
                "<p>Click below to confirm your registration:</p>" +
                "<a href=\"http://localhost:8080/auth/registration/confirm/" + id + "\" " +
                "style=\"display: inline-block; padding: 10px 20px; font-size: 16px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; border: 1px solid #007bff;\">Confirm</a>" +
                "</body></html>";
        emailSendingService.sendSimpleMessage(email, "Kecha BARCA yorvordiyu", emailContent);
        requestTime = LocalDateTime.now();
        return "Mail was sent";

    }

    public String registrationConfirm(Integer id, LocalDateTime clickTime){
        ProfileEntity entity = profileRepository.findById(id).get();
        if(entity.getStatus()!= ProfileStatus.IN_REGISTRATION){
            return "Your status is not in registration";
        }
        if(clickTime.minusSeconds(20).isAfter(requestTime)){
            return "Time was over to confirm your registration";
        }

        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.updateSome(entity);

        return "Registration completed";

    }


}
