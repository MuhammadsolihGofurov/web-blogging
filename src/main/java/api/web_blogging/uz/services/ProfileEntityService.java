package api.web_blogging.uz.services;

import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.exps.AppBadException;
import api.web_blogging.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileEntityService {

    @Autowired
    private ProfileRepository profileRepository;


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


}
