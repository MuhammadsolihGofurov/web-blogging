package api.web_blogging.uz.utils;


import api.web_blogging.uz.config.CustomUserDetails;
import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.ProfileRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

public class SpringSecurityUtil {

    public static CustomUserDetails getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        // System.out.println(user.getUsername());
        //Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();
        // Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return user;
    }

    public static Integer getCurrentProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getId();
    }


    public static Boolean hasRole(ProfileRole requiredRole) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<GrantedAuthority> roleList = (Collection<GrantedAuthority>) authentication.getAuthorities();

//        return roleList.stream().filter(sga -> sga.getAuthority().equals(requiredRole.name()))
//                .findAny().isPresent();

        return roleList.stream().anyMatch(sga -> sga.getAuthority().equals(requiredRole.name()));

    }

}
