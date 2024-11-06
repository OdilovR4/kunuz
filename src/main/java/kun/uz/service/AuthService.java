package kun.uz.service;

import kun.uz.dto.profile.AuthDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.dto.profile.RegistrationDTO;
import kun.uz.dto.profile.SmsConfirmDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.JwtUtil;
import kun.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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
    @Autowired
    private AttachService attachService;



    public String registration(RegistrationDTO dto) {
        ProfileEntity usernameEntity = profileRepository.getByUsername(dto.getUsername());

        if (usernameEntity != null) {
            switch (usernameEntity.getStatus()) {
                case ACTIVE -> throw new AppBadRequestException("This username already in use");
                case BLOCKED -> throw new AppBadRequestException("This username is blocked");
                case IN_REGISTRATION -> {
                    if (dto.getUsername().endsWith("@gmail.com")) {
                        return emailService.sendToEmail(usernameEntity.getId(), dto.getUsername());
                    } else {
                        return smsService.sendRegistrationSms(dto.getUsername());
                    }
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

    public ProfileDTO login(AuthDTO dto){
        ProfileEntity entity = profileRepository.getByUsername(dto.getUsername());
        if(entity==null) {
            throw new AppBadRequestException("Invalid username or password");
        }
        if(!entity.getPassword().equals(MD5Util.md5(dto.getPassword()))) {
            throw new AppBadRequestException("Invalid username or password");
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
        profileDTO.setPhoto(attachService.getDto(entity.getPhotoId()));

        // 1. findByPhone()
        // 2. check IN_REGISTRATION

        // check()
        // 3. check code is correct
        // 4. sms expiredTime
        // 5. attempt count  (10,000 - 99,999)
        // change status and update

        return profileDTO;
    }

}
