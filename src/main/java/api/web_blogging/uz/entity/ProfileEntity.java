package api.web_blogging.uz.entity;


import api.web_blogging.uz.enums.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "profile")
@Getter
@Setter
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GeneralStatus status;

    @Column(name = "temp_username")
    private String tempUsername;


    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "post_count")
    private String postCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private AttachEntity photo;

    // roles //
    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<ProfileRoleEntity> roleList;

    //    Created Date  //
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
