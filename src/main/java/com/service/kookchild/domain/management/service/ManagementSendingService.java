package com.service.kookchild.domain.management.service;

import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface ManagementSendingService {
    void updateParentBalance(String parentId);
    void updateChildBalance(String childId);
    public FindAccountResponse sendMoney(FindAccountInfoPair fi);
}
