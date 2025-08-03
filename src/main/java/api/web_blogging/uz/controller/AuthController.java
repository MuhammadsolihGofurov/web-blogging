package api.web_blogging.uz.controller;


import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.LoginDto;
import api.web_blogging.uz.dto.ProfileDto;
import api.web_blogging.uz.dto.auth.ResetPasswordConfirmDTO;
import api.web_blogging.uz.dto.auth.ResetPasswordDTO;
import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.dto.sms.SmsResendDTO;
import api.web_blogging.uz.dto.sms.SmsVerificationDTO;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth APIs", description = "API list for managing Auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "register", description = "Api for register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody registerDTO registerDto,
                                               @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.registration(registerDto, lang));
    }

    @GetMapping("/register/verification/{token}")
    @Operation(summary = "Email Verification", description = "Api for verify email")
    public ResponseEntity<String> emailVerification(@PathVariable("token") String token) {
        return ResponseEntity.ok().body(authService.regVerification(token));
    }

    @PostMapping("/register/sms-verification")
    @Operation(summary = "sms verification", description = "Api for sms verify")
    public ResponseEntity<String> smsVerifiation(@RequestBody SmsVerificationDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.smsVerification(dto, lang));
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Api for reset password")
    public ResponseEntity<AppResponse> resetPassword(@Valid @RequestBody ResetPasswordDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.resetPassword(dto, lang));
    }

    @PostMapping("/reset-password-confirm")
    @Operation(summary = "Reset password confirm", description = "Api for Reset password confirm")
    public ResponseEntity<AppResponse> resetPasswordConfirm(@Valid @RequestBody ResetPasswordConfirmDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.resetPasswordConfirm(dto, lang));
    }

    @PostMapping("/register/sms-verification-resend")
    @Operation(summary = "Sms verify resed", description = "Api for sms verify resend")
    public ResponseEntity<String> smsVerifiationResend(@RequestBody SmsResendDTO dto, @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.smsResend(dto, lang));
    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "Api for login")
    public ResponseEntity<ProfileDto> loginUser(@Valid @RequestBody LoginDto loginDto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLang lang) {
        return ResponseEntity.ok().body(authService.login(loginDto, lang));
    }



}
