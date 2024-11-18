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
    @Autowired
    private ResourceBundleService resourceBundleService;

    public String registration(RegistrationDTO dto, String lang) {
        ProfileEntity usernameEntity = profileRepository.getByUsername(dto.getUsername());

        if (usernameEntity != null) {
            switch (usernameEntity.getStatus()) {
                case ACTIVE -> throw new AppBadRequestException(resourceBundleService.getMessage("username.in.use",lang));
                case BLOCKED -> throw new AppBadRequestException(resourceBundleService.getMessage("username.blocked",lang));
                case IN_REGISTRATION -> {
                    if (dto.getUsername().endsWith("@gmail.com")) {
                        return emailService.sendToEmail(usernameEntity.getId(), dto.getUsername(),lang);
                    } else {
                        return smsService.sendRegistrationSms(dto.getUsername(),lang);
                    }
                }
            }
        }

        if (dto.getUsername().endsWith("@gmail.com")) {
            return emailService.createByEmail(dto,lang);
        } else {
            return smsService.createByPhone(dto,lang);
        }
    }



    public String emailConfirm(Integer id, LocalDateTime clickTime,String lang) {
        return emailService.emailConfirm(id, clickTime,lang);
    }

    public String smsConfirm(SmsConfirmDTO dto, LocalDateTime clickTime, String lang) {
        return smsService.smsConfirm(dto, clickTime,lang);
    }

    public ProfileDTO login(AuthDTO dto, String lang) {
        ProfileEntity entity = profileRepository.getByUsername(dto.getUsername());
        if(entity==null) {
            throw new AppBadRequestException(resourceBundleService.getMessage("email.password.wrong", lang));
        }
        if(!entity.getPassword().equals(MD5Util.md5(dto.getPassword()))) {
            throw new AppBadRequestException(resourceBundleService.getMessage("email.password.wrong", lang));
        }
        if(!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadRequestException(resourceBundleService.getMessage("not.active", lang));
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setName(entity.getName());
        profileDTO.setRole(entity.getRole());
        profileDTO.setUsername(entity.getUsername());
        profileDTO.setSurname(entity.getSurname());
        profileDTO.setJwtToken(JwtUtil.encode(entity.getUsername(),entity.getRole().toString()));
        profileDTO.setPhoto(attachService.getDto(entity.getPhotoId()));

        return profileDTO;
    }

}
