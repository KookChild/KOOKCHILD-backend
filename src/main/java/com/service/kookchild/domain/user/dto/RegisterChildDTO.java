package com.service.kookchild.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterChildDTO {
    private String email;
    private String password;
    private String name;
    private String phoneNum;
    private String ssn;
    private LocalDateTime birthdate;
    private List<RegisterRequestDTO> childList;

    private int level1Reward;
    private int level2Reward;
    private int level3Reward;
}
