package api.web_blogging.uz.config;

import api.web_blogging.uz.entity.ProfileEntity;
import api.web_blogging.uz.enums.GeneralStatus;
import api.web_blogging.uz.enums.ProfileRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class CustomUserDetails implements UserDetails {
    private String name;
    private String password;
    private Integer id;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private GeneralStatus status;

    public CustomUserDetails(ProfileEntity profile, List<ProfileRole> roleLists) {
        this.name = profile.getName();
        this.password = profile.getPassword();
        this.id = profile.getId();
        this.username = profile.getUsername();
        this.status = profile.getStatus();

//        List<SimpleGrantedAuthority> roles = new ArrayList<>();
//        for (ProfileRole role : roleLists) {
//            roles.add(new SimpleGrantedAuthority(role.name()));
//        }
//
//        this.authorities = roles;

        this.authorities = roleLists.stream().map(item -> new SimpleGrantedAuthority(item.toString())).toList();

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status.equals(GeneralStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
