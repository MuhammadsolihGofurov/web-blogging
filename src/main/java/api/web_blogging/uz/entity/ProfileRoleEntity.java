package api.web_blogging.uz.entity;


import api.web_blogging.uz.enums.ProfileRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Table(name = "profileRoleEntity")
@Entity
@Getter
@Setter
public class ProfileRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile", insertable = false, updatable = false)
    private ProfileEntity profile;


    @Column(name = "profile_id")
    private Integer profileId;

    @Column(name = "roles")
    @Enumerated(EnumType.STRING)
    private ProfileRole roles;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
