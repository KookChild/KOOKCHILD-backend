package com.service.kookchild.domain.user.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder @Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private String ssn;
    private LocalDateTime birthdate;
    private List<RegisterChildDTO> childList;

    private String accountPassword;
}
