package kun.uz.service;

import kun.uz.dto.profile.RegistrationDTO;
import kun.uz.entity.EmailHistoryEntity;
import kun.uz.entity.ProfileEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.repository.EmailHistoryRepository;
import kun.uz.repository.ProfileRepository;
import kun.uz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Value("${server.domain}")
    private String domainName;
    @Autowired
    private ResourceBundleService resourceBundleService;

    public String createByEmail(RegistrationDTO dto, String lang) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setUsername(dto.getUsername());
        entity.setSurname(dto.getSurname());
        entity.setPassword(MD5Util.md5(dto.getPassword()));
        entity.setVisible(Boolean.TRUE);
        entity.setStatus(ProfileStatus.IN_REGISTRATION);
        entity.setCreatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return sendToEmail(entity.getId(), dto.getUsername(), lang);
    }

    public String sendToEmail(Integer id, String email, String lang) {
        String title = "Kecha Barca yevordiku Realni jigarr";
        if (sendingService.sendSimpleMessage(email, title, getConfirmationButton(id))) {
            emailHistoryService.addEmailHistory(email, title, LocalDateTime.now());
            return "Confirmation code was sent to " + email;
        }

        throw new AppBadRequestException(resourceBundleService.getMessage("not.sent.more.email", lang));

    }

    public String emailConfirm(Integer id, LocalDateTime clickTime, String lang) {
        ProfileEntity entity = profileRepository.findById(id).get();
        if (entity.getStatus() != ProfileStatus.IN_REGISTRATION) {
            throw new AppBadRequestException(resourceBundleService.getMessage("status.not.registration", lang));
        }

        List<EmailHistoryEntity> emailHistoryEntity = emailHistoryRepository.getLastSentEmail(entity.getUsername());
        LocalDateTime sentDate = emailHistoryEntity.get(0).getCreatedDate();

        if (clickTime.minusSeconds(120).isAfter(sentDate)) {
            throw new AppBadRequestException(resourceBundleService.getMessage("time.over", lang));
        }

        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setRole(ProfileRole.ROLE_USER);
        profileRepository.updateSome(entity);

        return "Registration successfully completed";
    }

    public String getConfirmationButton(Integer id) {
        return "<html><body>" +
                "<p>Click below to confirm your registration:</p>" +
                "<a href=\"" + domainName + "/auth/registration/confirm/" + id + "\" " +
                "style=\"display: inline-block; padding: 10px 20px; font-size: 16px; background-color:" +
                " #007bff; color: white; text-decoration: none; border-radius: 5px; border: " +
                "1px solid #007bff;\">Confirm</a> </body></html>";

    }
}
