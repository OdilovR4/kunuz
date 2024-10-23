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

    public String registration(RegistrationDTO dto){
        //check email
        ProfileEntity entity = new ProfileEntity();
        entity.setEmail(dto.getEmail());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.md5(dto.getPassword()));
        entity.setStatus(ProfileStatus.IN_REGISTRATION);
        entity.setVisible(Boolean.TRUE);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.save(entity);

        String emailContent = "<html><body>" +
                "<p>Click below to confirm your registration:</p>" +
                "<a href=\"http://localhost:8080/auth/registration/confirm/" + entity.getId() + "\" " +
                "style=\"display: inline-block; padding: 10px 20px; font-size: 16px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; border: 1px solid #007bff;\">Confirm</a>" +
                "</body></html>";


        emailSendingService.sendSimpleMessage(dto.getEmail(), "Futbol", emailContent);
        return "Mail was sent";


    }

    public String registrationConfirm(Integer id){
        ProfileEntity entity = profileRepository.findByIdAndVisibleTrue(id);
        if(entity == null){
            return "Not completed";
        }
        if(entity.getStatus()!= ProfileStatus.IN_REGISTRATION){
            return "Not completed";
        }
        entity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity);
        return "Registration completed";

    }
}
