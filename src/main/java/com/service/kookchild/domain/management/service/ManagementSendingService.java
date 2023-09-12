package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountDTO;

public interface ManagementSendingService {
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi);
    public FindAccountDTO checkChildMoney(FindAccountInfoPair fi);

    public Long FindConsumption(FindAccountInfoPair fi);
}
