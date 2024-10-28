package kun.uz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kun.uz.dto.RegistrationDTO;
import kun.uz.dto.SmsAuthResponseDTO;
import kun.uz.dto.SmsConfirmDTO;
import kun.uz.entity.ProfileEntity;
import kun.uz.entity.SmsHistoryEntity;
import kun.uz.entity.TokenEntity;
import kun.uz.enums.ProfileRole;
import kun.uz.enums.ProfileStatus;
import kun.uz.enums.SmsStatus;
import kun.uz.exceptions.AppBadRequestException;
import kun.uz.repository.ProfileRepository;
import kun.uz.repository.SmsHistoryRepository;
import kun.uz.repository.TokenRepository;
import kun.uz.util.MD5Util;
import kun.uz.util.RandomUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SmsService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private SmsHistoryRepository smsHistoryRepository;
    @Autowired
    private ProfileRepository profileRepository;

    public String sendRegistrationSms(String phoneNumber) {
        int code = RandomUtil.getRandomInt(5);
        String message = "This is test from Eskiz"; //+code qilib , code ni ham qushib berish kerak edi
        saveSmsHistory(phoneNumber, message, code);
        sendSms(phoneNumber, message);
        return "confirmation code was sent to " + phoneNumber;
    }

    private void sendSms(String phone, String message) {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("mobile_phone", phone)
                    .add("message", message)
                    .add("from", "4546")
                    .build();

            Request request = new Request.Builder()
                    .url("https://notify.eskiz.uz/api/message/sms/send")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + getToken())
                    .post(formBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void saveSmsHistory(String phone, String message, Integer code) {
        Long count = smsHistoryRepository.getCountSms(phone, LocalDateTime.now().minusMinutes(1), LocalDateTime.now());
        if (count >= 3) {
            throw new AppBadRequestException("You can not send more than 3 sms in one minute ");
        }

        SmsHistoryEntity historyEntity = new SmsHistoryEntity();
        historyEntity.setMessage(message);
        historyEntity.setStatus(SmsStatus.NEW);
        historyEntity.setPhone(phone);
        historyEntity.setSmsCode(code);
        historyEntity.setAttemptCount(0);
        historyEntity.setSendTime(LocalDateTime.now());
        smsHistoryRepository.save(historyEntity);
    }

    private String getNewToken() {
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", "odilovrahmatullo5822@gmail.com")
                    .add("password", "IjQw04tzdd9tbnQBu93O0DsGT5fXB9usUJ0virjB")
                    .add("from", "4546")
                    .build();

            Request request = new Request.Builder()
                    .url("https://notify.eskiz.uz/api/auth/login")
                    .post(formBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();

            ObjectMapper mapper = new ObjectMapper();
            SmsAuthResponseDTO obj = mapper.readValue(response.body().string(), SmsAuthResponseDTO.class);
            return obj.getData().getToken();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getToken() {
        List<TokenEntity> tokenList = tokenRepository.getByDate();
        if (tokenList.isEmpty()) {
            String token = getNewToken();
            TokenEntity tokenEntity = new TokenEntity();
            tokenEntity.setToken(token);
            tokenEntity.setDate(LocalDate.now());
            tokenRepository.save(tokenEntity);
            return token;
        } else {
            TokenEntity tokenEntity = tokenList.get(0);
            LocalDate before29Days = LocalDate.now().minusDays(29);

            if (tokenEntity.getDate().isBefore(before29Days)) {
                String token = getNewToken();
                tokenEntity.setToken(token);
                tokenEntity.setDate(LocalDate.now());
                tokenRepository.save(tokenEntity);
                return token;
            }

            return tokenEntity.getToken();
        }
    }

    public String createByPhone(RegistrationDTO dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());
        entity.setPassword(MD5Util.md5(dto.getPassword()));
        entity.setVisible(Boolean.TRUE);
        entity.setStatus(ProfileStatus.IN_REGISTRATION);
        entity.setCreatedDate(LocalDateTime.now());
        profileRepository.save(entity);
        return sendRegistrationSms(entity.getUsername());
    }

    public String smsConfirm(SmsConfirmDTO dto, LocalDateTime clickTime) {
        ProfileEntity entity = profileRepository.findByUsername(dto.getPhone());
        if (entity == null) {
            return "Number not found";
        }
        if (entity.getStatus() != ProfileStatus.IN_REGISTRATION) {
            return "Your status is not in registration";
        }
        return check(dto, clickTime);
    }

    public String check(SmsConfirmDTO dto, LocalDateTime clickTime) {
        SmsHistoryEntity entity = smsHistoryRepository.getByPhone(dto.getPhone()).get(0);
        if (entity == null) {
            throw new AppBadRequestException("Sms not sent to " + dto.getPhone());
        }
        if (entity.getAttemptCount() >= 3) {
            throw new AppBadRequestException("Your limits are up");
        }
        if (!(entity.getSmsCode().equals(dto.getCode()))) {
            smsHistoryRepository.increaseAttempt(entity.getId());
            throw new AppBadRequestException("Sms code is incorrect");
        }

        if (clickTime.minusMinutes(3).isAfter(entity.getSendTime())) {
            throw new AppBadRequestException("Time is up to confirm code ");
        }
        entity.setStatus(SmsStatus.USED);
        ProfileEntity entity1 = profileRepository.findByUsername(dto.getPhone());
        entity1.setRole(ProfileRole.ROLE_USER);
        entity1.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(entity1);
        return "Registration successful finished ";
    }
}
