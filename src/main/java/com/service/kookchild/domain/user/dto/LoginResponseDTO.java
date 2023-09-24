package com.service.kookchild.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String email;
    private String name;
    private boolean isParent;
    private String token;
}
