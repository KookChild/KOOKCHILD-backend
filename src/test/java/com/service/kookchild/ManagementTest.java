package com.service.kookchild;

import com.service.kookchild.domain.management.dto.FindAccountResponse;
import com.service.kookchild.domain.management.service.ManagementService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest@Transactional
@Commit
public class ManagementTest {

    @Autowired
    private ManagementService managementService;

    @Test
    void test(){
        FindAccountResponse accountInfo = managementService.getAccountInfo("hjkim2902@gmail.com");
        System.out.println(accountInfo);
    }

}
