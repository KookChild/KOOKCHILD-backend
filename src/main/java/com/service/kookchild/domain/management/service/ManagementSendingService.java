package com.service.kookchild.domain.management.service;

import antlr.collections.List;
import com.service.kookchild.domain.management.dto.*;

import java.util.ArrayList;

public interface ManagementSendingService {
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi);
    public CheckChildMoneyResponse checkChildMoney(FindAccountInfoPair fi);
    public Long findUserId(String email);
    public String findUserNameById(Long id);



    public ArrayList<FindAccountChildNameId> findChildNamesByParentId(Long id);
}
