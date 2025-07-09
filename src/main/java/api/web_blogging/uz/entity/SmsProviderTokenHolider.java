package api.web_blogging.uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "sms_provider_token_holder_entity")
public class SmsProviderTokenHolider {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "token")
    private String token;

    //    Created Date  //
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
