package com.service.kookchild.domain.management.service;

import antlr.collections.List;
import com.service.kookchild.domain.management.dto.FindAccountAmount;
import com.service.kookchild.domain.management.dto.FindAccountInfoPair;
import com.service.kookchild.domain.management.dto.FindAccountResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

public interface ManagementSendingService {
    public Long findUserId(String email);
    public String findUserNameById(Long id);
    public FindAccountResponse sendChildMoney(FindAccountInfoPair fi);
    public FindAccountResponse checkChildMoney(FindAccountInfoPair fi);


    public ArrayList<String> findChildNamesByParentId(Long id);
}
