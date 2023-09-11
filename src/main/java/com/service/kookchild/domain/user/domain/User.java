package com.service.kookchild.domain.user.domain;

import com.service.kookchild.global.domain.BaseEntity;
import lombok.*;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private String ssn;
    private boolean isParent;
    private LocalDateTime birthdate;

    @Builder
    public User(String email, String password, String name, String phoneNum, String ssn, boolean isParent, LocalDateTime birthdate){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNum = phoneNum;
        this.ssn = ssn;
        this.isParent = isParent;
        this.birthdate = birthdate;
    }


}
