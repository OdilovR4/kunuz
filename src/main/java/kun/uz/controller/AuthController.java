package kun.uz.controller;

import kun.uz.dto.RegistrationDTO;
import kun.uz.dto.SmsConfirmDTO;
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
    public ResponseEntity<String> registration (@RequestBody RegistrationDTO dto){
        return ResponseEntity.ok(authService.registration(dto));
    }

    @GetMapping("/registration/confirm/{id}")
    public ResponseEntity<String> registrationEmailConfirm(@PathVariable Integer id){
        return ResponseEntity.ok(authService.emailConfirm(id,LocalDateTime.now()));
    }

   @PostMapping("/registration/confirm/sms")
    public ResponseEntity<String> registrationSmsConfirm (@RequestBody SmsConfirmDTO dto){
        return ResponseEntity.ok(authService.smsConfirm(dto,LocalDateTime.now()));

    }



}
