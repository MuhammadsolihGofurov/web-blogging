package api.web_blogging.uz.controller;

import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import api.web_blogging.uz.services.ProfileRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/init")
public class InitController {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileRoleService profileRoleService;

    @GetMapping("/all")
    public String initAdmin() {
        Optional<ProfileEntity> profileEntity = profileRepository.findByUsernameAndVisibleTrue("222211m@jdu.uz");

        if (profileEntity.isPresent()) {
            throw new AppBadException("Bad request");
        }

        ProfileEntity profile = new ProfileEntity();
        profile.setName("admin");
        profile.setPassword(bCryptPasswordEncoder.encode("secret"));
        profile.setUsername("222211m@jdu.uz");
        profile.setStatus(GeneralStatus.ACTIVE);
        profile.setCreatedAt(LocalDateTime.now());

        profileRepository.save(profile);

//        profileRoleService.createProfileRole(profile.getId(), ProfileRole.ROLE_USER);
        profileRoleService.createProfileRole(profile.getId(), ProfileRole.ROLE_ADMIN);

        return "Success";
    }

}
