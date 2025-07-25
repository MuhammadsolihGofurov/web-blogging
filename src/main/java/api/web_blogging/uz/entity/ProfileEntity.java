package api.web_blogging.uz.entity;


import api.web_blogging.uz.enums.GeneralStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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


    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;


    // roles //


    //    Created Date  //
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
