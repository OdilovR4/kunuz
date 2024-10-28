package kun.uz.service;

import kun.uz.dto.RegistrationDTO;
import kun.uz.dto.SmsConfirmDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private SmsService smsService;
    @Autowired
    EmailService emailService;


    public String registration(RegistrationDTO dto) {
        ProfileEntity usernameEntity = profileRepository.getByUsername(dto.getUsername());
        if (usernameEntity != null) {
            if (usernameEntity.getStatus() == ProfileStatus.ACTIVE) {
                throw new AppBadRequestException("This username already in use");
            }
            if(usernameEntity.getStatus()==ProfileStatus.BLOCKED) {
                throw new AppBadRequestException("This username is blocked");
            }
            if (usernameEntity.getStatus() == ProfileStatus.IN_REGISTRATION) {
                if (dto.getUsername().endsWith("@gmail.com")) {
                  return emailService.sendToEmail(usernameEntity.getId(), dto.getUsername());
                }
                else {
                    return smsService.sendRegistrationSms(dto.getUsername());
                }
            }

        }
        if (dto.getUsername().endsWith("@gmail.com")) {
            return emailService.createByEmail(dto);
        } else {
           return smsService.createByPhone(dto);
        }
    }


    public String emailConfirm(Integer id, LocalDateTime clickTime) {
        return emailService.emailConfirm(id, clickTime);
    }

    public String smsConfirm(SmsConfirmDTO dto, LocalDateTime clickTime) {
        return smsService.smsConfirm(dto, clickTime);

        // 1. findByPhone()
        // 2. check IN_REGISTRATION

        // check()
        // 3. check code is correct
        // 4. sms expiredTime
        // 5. attempt count  (10,000 - 99,999)
        // change status and update

    }

}
