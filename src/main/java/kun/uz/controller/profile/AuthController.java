package kun.uz.controller.profile;

import jakarta.validation.Valid;
import kun.uz.dto.profile.AuthDTO;
import kun.uz.dto.profile.ProfileDTO;
import kun.uz.dto.profile.RegistrationDTO;
import kun.uz.dto.profile.SmsConfirmDTO;
import kun.uz.enums.AppLanguage;
import kun.uz.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody RegistrationDTO dto,
                                               @RequestHeader(value = "Accept-language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(authService.registration(dto, lang.name()));
    }

    @GetMapping("/registration/confirm/{id}")
    public ResponseEntity<String> registrationEmailConfirm(@PathVariable Integer id,
                                                           @RequestHeader(value = "Accept-language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(authService.emailConfirm(id, LocalDateTime.now(),lang.name()));
    }

    @PostMapping("/registration/confirm/sms")
    public ResponseEntity<String> registrationSmsConfirm(@RequestBody SmsConfirmDTO dto,
                                                         @RequestHeader(value = "Accept-language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(authService.smsConfirm(dto, LocalDateTime.now(),lang.name()));

    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDTO> login(@RequestBody @Valid AuthDTO dto,
                                            @RequestHeader(value = "Accept-language", defaultValue = "uz") AppLanguage lang) {
        return ResponseEntity.ok(authService.login(dto, lang.name()));
    }
}