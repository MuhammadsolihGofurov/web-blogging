package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.AppResponse;
import api.web_blogging.uz.dto.ProfileDto;
import api.web_blogging.uz.dto.profile.*;
import api.web_blogging.uz.dto.sms.CodeDTO;
import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.AppLang;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import api.web_blogging.uz.repository.ProfileRoleRepository;
import api.web_blogging.uz.utils.EmailUtil;
import api.web_blogging.uz.utils.JwtUtil;
import api.web_blogging.uz.utils.PhoneUtil;
import api.web_blogging.uz.utils.SpringSecurityUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileEntityService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ResourceBundleService getMessageService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SmsSendHistoryService smsSendHistoryService;

    @Autowired
    private EmailSendingService emailSendingService;

    @Autowired
    private SmsSendService smsSendService;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    @Autowired
    private AttachService attachService;


    public AppResponse<String> updateProfile(ProfileDetailUpdateDTO dto, AppLang lang) {
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        profileRepository.updateProfileName(userId, dto.getName());

        return new AppResponse<>(getMessageService.getMessage("updated.profile.name", lang));
    }

    public ProfileEntity getById(Integer id) {
//        Optional<ProfileEntity> profile = profileRepository.findByIdAndVisibleTrue(id);
//        if (!profile.isPresent()) {
//            throw new AppBadException("Profile not found");
//        }
//
//
//        return profile.get();

        return profileRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Profile not found");
        });


    }

    public AppResponse<String> updateProfileWithPassword(ProfileDetailUpdatePasswordDTO dto, AppLang lang) {
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        ProfileEntity profile = getById(userId);

        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), profile.getPassword())) {
            throw new AppBadException(getMessageService.getMessage("password.not.matches", lang));
        }

        profileRepository.updatePassword(userId, bCryptPasswordEncoder.encode(dto.getCurrentPassword()));

        return new AppResponse<>(getMessageService.getMessage("updated.profile.password", lang));
    }

    public AppResponse<String> updateProfileWithUsername(ProfileDetailUpdateUsernameDTO dto, AppLang lang) {
//        check
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());

        if (optional.isPresent()) {
            throw new AppBadException(getMessageService.getMessage("username.exists", lang));
        }

//        send sms or mail
        if (PhoneUtil.isValidPhone(dto.getUsername())) {
            smsSendService.sendUsernmaeChangeConfirmSms(dto.getUsername());
        } else if (EmailUtil.isValidEmail(dto.getUsername())) {
//            email uchun history yozish kerak va ularni emailga yuborish kerak so'ng unda ham check methodini yozish kerak.
            emailSendingService.sendResetEmail(dto.getUsername());
        }

//        save
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        profileRepository.updateProfileTempUsername(userId, dto.getUsername());

        return new AppResponse<>(getMessageService.getMessage("updated.profile.username", lang));
    }

    public AppResponse<String> updateProfileWithUsernameConfirm(CodeDTO dto, AppLang lang) {
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        ProfileEntity profile = getById(userId);

        String tempUsername = profile.getTempUsername();

        if (PhoneUtil.isValidPhone(tempUsername)) {
            smsSendHistoryService.check(tempUsername, dto.getCode());
        } else if (EmailUtil.isValidEmail(tempUsername)) {
//            email uchun history yozish kerak va ularni emailga yuborish kerak so'ng unda ham check methodini yozish kerak.
            emailSendingService.sendResetEmail(tempUsername);
        }

//        update username
        profileRepository.updateUsername(userId, tempUsername);
        List<ProfileRole> roles = profileRoleRepository.getAllRolesListByProfileId(userId);
        String jwt = JwtUtil.encode(tempUsername, profile.getId(), roles);

        return new AppResponse<>(getMessageService.getMessage("updated.profile.username", lang), jwt);
    }

    public AppResponse<String> updateProfilePhoto(ProfileDetailUpdatePhotoDTO dto, AppLang lang) {
        Integer userId = SpringSecurityUtil.getCurrentProfileId();
        ProfileEntity profile = getById(userId);

        if (profile.getPhotoId() != null && !profile.getPhotoId().equals(dto.getAttachId())) {
            attachService.delete(profile.getPhotoId());
        }


        profileRepository.updatePhoto(userId, dto.getAttachId());


        return new AppResponse<>("Update Profile Photo");
    }

    public PageImpl<ProfileDto> filterForAdmin(ProfileFilterDTO dto, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ProfileEntity> filterResult = null;

        if (dto.getQuery() == null) {
            filterResult = profileRepository.findAllByVisibleIsTrueOrderByCreatedAtDesc(pageable);
        } else {
            filterResult = profileRepository.filterWithQuery("%" + dto.getQuery().toLowerCase() + "%", pageable);
        }

        List<ProfileDto> resultsList = filterResult.stream().map(this::toDTO).toList();

        return new PageImpl<>(resultsList, pageable, filterResult.getTotalElements());
    }

    public ProfileDto toDTO(ProfileEntity profile) {
        ProfileDto dto = new ProfileDto();
        dto.setName(profile.getName());
        dto.setUsername(profile.getUsername());
        if (profile.getRoleList() != null) {
            List<ProfileRole> roleList = profile.getRoleList().stream().map(ProfileRoleEntity::getRoles)
                    .toList();
            dto.setRoles(roleList);
        }
        dto.setStatus(profile.getStatus());
        dto.setPhotoUrl(attachService.openURL(profile.getPhotoId()));
        return dto;
    }

    public AppResponse<String> changeStatusProfile(Integer id, ChangeProfileStatusDTO dto) {
        profileRepository.updateProfileStatus(id, dto.getStatus());
        return new AppResponse<>("Successfully changed");
    }

    public AppResponse<String> deleteProfile(Integer id) {
        ProfileEntity profile = getById(id);
        if (profile == null) {
            throw new AppBadException("Profile not found");
        }

        profileRepository.updateVisibleIsFalse(id);

        return new AppResponse<>("Successfully deleted");

    }
}
