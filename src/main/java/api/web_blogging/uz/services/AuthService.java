package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.LoginDto;
import api.web_blogging.uz.dto.ProfileDto;
import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import api.web_blogging.uz.repository.ProfileRoleRepository;
import api.web_blogging.uz.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
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
                throw new AppBadException(getMessageService.getMessage("email.phone.exists" , lang));
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

        // send email code
//        smsSendService.sendRegistrationMessage(profileEntity.getUsername());
        emailSendingService.sendRegistrationEmail(registerDto.getUsername(), profileEntity.getId());

        return getMessageService.getMessage("register.success" , lang);
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

    public ProfileDto login(LoginDto loginDto, AppLang lang) {
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(loginDto.getUsername());
        ProfileEntity profile = optional.get();

        if (optional.isEmpty()) {
            throw new AppBadException(getMessageService.getMessage("username.passwrod.wrong" , lang));
        }
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), profile.getPassword())) {
            throw new AppBadException(getMessageService.getMessage("username.passwrod.wrong" , lang));
        }
        if (!profile.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadException(getMessageService.getMessage("status.wrong" , lang));
        }

        //  response

        ProfileDto newDto = new ProfileDto();
        newDto.setUsername(profile.getUsername());
        newDto.setName(profile.getName());
        newDto.setRoles(profileRoleRepository.getAllRolesListByProfileId(profile.getId()));
        //  jwt
        newDto.setToken(JwtUtil.encode(profile.getUsername(), profile.getId(), newDto.getRoles()));


        return newDto;
    }

}
