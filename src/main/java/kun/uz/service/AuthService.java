package kun.uz.service;

<<<<<<< HEAD
import kun.uz.dto.AuthDTO;
import kun.uz.dto.ProfileDTO;
=======
>>>>>>> ff86d3875ead696e854446a49c98943d12e9089b
import kun.uz.dto.RegistrationDTO;
import kun.uz.dto.SmsConfirmDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.repository.ProfileRepository;
<<<<<<< HEAD
import kun.uz.util.JwtUtil;
import kun.uz.util.MD5Util;
=======
>>>>>>> ff86d3875ead696e854446a49c98943d12e9089b
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
<<<<<<< HEAD
        // 1. findByPhone()
        // 2. check IN_REGISTRATION

        // check()
        // 3. check code is correct
        // 4. sms expiredTime
        // 5. attempt count  (10,000 - 99,999)
        // change status and update
    }

    public ProfileDTO login(AuthDTO dto){
        ProfileEntity entity = profileRepository.getByUsername(dto.getUsername());
        if(entity==null) {
            throw new AppBadRequestException("1 Invalid username or password");
        }
        if(!entity.getPassword().equals(MD5Util.md5(dto.getPassword()))) {
            throw new AppBadRequestException("2 Invalid username or password");
        }
        if(!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadRequestException("User is not active");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(entity.getName());
        profileDTO.setRole(entity.getRole());
        profileDTO.setUsername(entity.getUsername());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setJwtToken(JwtUtil.encode(entity.getUsername(),entity.getRole().toString()));
=======

        // 1. findByPhone()
        // 2. check IN_REGISTRATION

        // check()
        // 3. check code is correct
        // 4. sms expiredTime
        // 5. attempt count  (10,000 - 99,999)
        // change status and update
>>>>>>> ff86d3875ead696e854446a49c98943d12e9089b

        return profileDTO;
    }

}
