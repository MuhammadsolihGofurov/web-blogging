package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.LoginDto;
import api.web_blogging.uz.dto.ProfileDto;
import api.web_blogging.uz.dto.auth.ResetPasswordConfirmDTO;
import api.web_blogging.uz.dto.auth.ResetPasswordDTO;
import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.dto.sms.SmsResendDTO;
import api.web_blogging.uz.dto.sms.SmsVerificationDTO;
import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import api.web_blogging.uz.repository.ProfileRoleRepository;
import api.web_blogging.uz.utils.EmailUtil;
import api.web_blogging.uz.utils.JwtUtil;
import api.web_blogging.uz.utils.PhoneUtil;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ProfileRoleService profileRoleService;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private ProfileEntityService profileEntityService;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    @Autowired
    private ResourceBundleService getMessageService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private SmsSendHistoryService smsSendHistoryService;

    @Autowired
    private AttachService attachService;

    public String registration(registerDTO registerDto, AppLang lang) {
        // check if profile is exist
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(registerDto.getUsername());
        if (optional.isPresent()) {
            ProfileEntity profileEntity = optional.get();
            if (profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                //  delete with roles
                profileRoleService.deleteProfileRoles(profileEntity.getId());
                //  delete
                profileRepository.delete(profileEntity);

                //  send resend sms
            } else {
                throw new AppBadException(getMessageService.getMessage("email.phone.exists", lang));
            }
        }

        // new entity for profile
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setUsername(registerDto.getUsername());
        profileEntity.setName(registerDto.getName());

        // need to do hashcode
        profileEntity.setPassword(bCryptPasswordEncoder.encode(registerDto.getPassword()));
        profileEntity.setStatus(GeneralStatus.IN_REGISTRATION);
        profileEntity.setVisible(true);
        profileEntity.setCreatedAt(LocalDateTime.now());
        profileRepository.save(profileEntity);

        //  create role
        profileRoleService.createProfileRole(profileEntity.getId(), ProfileRole.ROLE_USER);

        // save entity with in_registration status

        // send email and phone code
        if (PhoneUtil.isValidPhone(profileEntity.getUsername())) {
            smsSendService.sendRegistrationMessage(profileEntity.getUsername());
        } else if (EmailUtil.isValidEmail(profileEntity.getUsername())) {
            emailSendingService.sendRegistrationEmail(registerDto.getUsername(), profileEntity.getId());
        }

        return getMessageService.getMessage("register.success", lang);
    }

    public String regVerification(String token) {
        try {
            Integer profileId = JwtUtil.decodeEmail(token);
            ProfileEntity profile = profileEntityService.getById(profileId);

            //  status check
            if (profile.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
                profileRepository.changeStatus(profileId, GeneralStatus.ACTIVE);
                return "Verification successfully activated";
            }
        } catch (JwtException e) {
            throw new AppBadException("Invalid token");
        }

        throw new AppBadException("Verification failed");
    }

    public String smsVerification(SmsVerificationDTO dto, AppLang lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());

        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("phone.not.found", lang));
        }
        ProfileEntity profileEntity = optional.get();

        if (!profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new AppBadException(getMessageService.getMessage("phone.not.found", lang));
        }

        if (!smsSendHistoryService.check(dto.getPhone(), dto.getCode())) {
            throw new AppBadException(getMessageService.getMessage("phone.already.sent", lang));
        }

        // code check via eskiz
        if (profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            profileRepository.changeStatus(profileEntity.getId(), GeneralStatus.ACTIVE);
            return "Verification successfully activated";
        }


        return "";
    }

    public ProfileDto login(LoginDto loginDto, AppLang lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(loginDto.getUsername());
        ProfileEntity profile = optional.get();

        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("username.passwrod.wrong", lang));
        }
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), profile.getPassword())) {
            throw new AppBadException(getMessageService.getMessage("username.passwrod.wrong", lang));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(getMessageService.getMessage("status.wrong", lang));
        }

        //  response

        ProfileDto newDto = new ProfileDto();
        newDto.setUsername(profile.getUsername());
        newDto.setName(profile.getName());
        newDto.setRoles(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        //  jwt
        newDto.setToken(JwtUtil.encode(profile.getUsername(), profile.getId(), newDto.getRoles()));
        String photoUrl = attachService.openURL(profile.getPhotoId());

        if (photoUrl != null) {
            newDto.setPhotoUrl(photoUrl);
        }


        return newDto;
    }

    public String smsResend(SmsResendDTO dto, AppLang lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getPhone());
        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("phone.not.found", lang));
        }
        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(GeneralStatus.IN_REGISTRATION)) {
            throw new AppBadException(getMessageService.getMessage("phone.not.found", lang));
        }

//        resend sms
        smsSendService.sendRegistrationMessage(dto.getPhone());

        return getMessageService.getMessage("sms.resend.success", lang);
    }

    public AppResponse<String> resetPassword(@Valid ResetPasswordDTO dto, AppLang lang) {
        String username = dto.getUsername();
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("username.not.found", lang));
        }

        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(getMessageService.getMessage("status.wrong", lang));
        }

        // send email and phone code
        if (PhoneUtil.isValidPhone(profileEntity.getUsername())) {
            smsSendService.sendResetMessage(profileEntity.getUsername());
        } else if (EmailUtil.isValidEmail(profileEntity.getUsername())) {
//            email uchun emailhistory ochish kerak shundagina codeni tekshirish imkoniyati bo'ladi xuddi smssendservicedagi kabi
            emailSendingService.sendResetEmail(dto.getUsername());
        }

        return new AppResponse(getMessageService.getMessage("sms.reset.success", lang));

    }

    public AppResponse<String> resetPasswordConfirm(@Valid ResetPasswordConfirmDTO dto, AppLang lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("phone.not.found", lang));
        }

        ProfileEntity profileEntity = optional.get();
        if (!profileEntity.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(getMessageService.getMessage("status.wrong", lang));
        }

        if (PhoneUtil.isValidPhone(profileEntity.getUsername())) {
            smsSendHistoryService.check(dto.getUsername(), dto.getCode());
        } else if (EmailUtil.isValidEmail(profileEntity.getUsername())) {
//            email uchun history yozish kerak va ularni emailga yuborish kerak so'ng unda ham check methodini yozish kerak.
            emailSendingService.sendResetEmail(dto.getUsername());
        }

        profileRepository.updatePassword(profileEntity.getId(), bCryptPasswordEncoder.encode(dto.getNewPassword()));


        return new AppResponse<>("Successfully change your password");
    }


}
