package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface ManagementSendingService {
    public Long findUserId(String email);
    public String findUserNameById(Long id);
    public FindAccountResponse sendChildMoney(FindAccountInfoPair fi);
    public FindAccountResponse checkChildMoney(FindAccountInfoPair fi);

    public Long FindConsumption(FindAccountInfoPair fi);
}
