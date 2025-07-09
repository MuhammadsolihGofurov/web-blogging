package api.web_blogging.uz.config;

import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.ProfileRole;
import api.web_blogging.uz.repository.ProfileRepository;
import api.web_blogging.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        // username = login or phone or email
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        ProfileEntity profile = optional.get();
        List<ProfileRole> roleList = profileRoleRepository.getAllRolesListByProfileId(profile.getId());

        return new CustomUserDetails(profile, roleList);
    }
}
