package com.service.kookchild.domain.mission.dto;

import com.service.kookchild.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChildListDTO {
    private long childId;
    private String childName;

    public static ChildListDTO of(User u) {
        return ChildListDTO.builder()
                .childId(u.getId())
                .childName(u.getName())
                .build();
    }
}
