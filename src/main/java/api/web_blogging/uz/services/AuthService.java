package api.web_blogging.uz.services;

import api.web_blogging.uz.dto.registerDTO;
import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private ProfileRoleService profileRoleService;

    public String registration(registerDTO registerDto) {
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
                throw new AppBadException("User already exists");
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

        // if entity saved with in_registration, resend email code

        //

        return "success";
    }

}
