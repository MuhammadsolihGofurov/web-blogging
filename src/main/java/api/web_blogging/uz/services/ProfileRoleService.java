package api.web_blogging.uz.services;

import api.web_blogging.uz.entity.ProfileRoleEntity;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProfileRoleService {

    @Autowired
    private ProfileRoleRepository profileRoleRepository;


    public void createProfileRole(Integer profileId, ProfileRole profileRole) {
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRoles(profileRole);
        entity.setCreatedAt(LocalDateTime.now());
        profileRoleRepository.save(entity);
    }

    public void deleteProfileRoles(Integer profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
