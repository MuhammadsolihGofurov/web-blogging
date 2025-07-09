package api.web_blogging.uz.utils;


import api.web_blogging.uz.config.CustomUserDetails;
import api.web_blogging.uz.entity.ProfileEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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

}
