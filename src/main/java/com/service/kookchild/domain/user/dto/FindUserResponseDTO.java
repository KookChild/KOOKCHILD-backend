package com.service.kookchild.domain.user.dto;

import com.service.kookchild.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FindUserResponseDTO {

    private String email;
    private String name;
    private String phoneNum;
    private String ssn;
    private boolean isParent;
    private LocalDateTime birthdate;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static FindUserResponseDTO from(User user){
        return FindUserResponseDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .phoneNum(user.getPhoneNum())
                .ssn(user.getSsn())
                .isParent(user.isParent())
                .birthdate(user.getBirthdate())
                .createdDate(user.getCreatedDate())
                .modifiedDate(user.getModifiedDate())
                .build();

    }
}
