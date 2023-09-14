package com.service.kookchild.domain.management.service;

import antlr.collections.List;
import com.service.kookchild.domain.management.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface ManagementSendingService {
    public FindAccountDTO sendChildMoney(FindAccountInfoPair fi);
    public Long findUserId(String email);
    public ArrayList<LocalDateTime> getLastDayOf();
    public FindAccountInformation findChildNamesByParentId(FindAccountInfoPair fi);
}
