package com.service.kookchild.domain.management.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@NoArgsConstructor
public class FindAccountAmount {
    private Long findConsumption;
    private Long findNotInConsumption;

    public FindAccountAmount(Long findConsumption, Long findNotInConsumption) {
        this.findConsumption = findConsumption;
        this.findNotInConsumption = findNotInConsumption;
    }
}
