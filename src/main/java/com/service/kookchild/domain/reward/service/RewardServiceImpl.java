package com.service.kookchild.domain.reward.service;


import com.service.kookchild.domain.management.domain.Account;
import com.service.kookchild.domain.management.domain.AccountHistory;
import com.service.kookchild.domain.management.domain.AccountType;
import com.service.kookchild.domain.management.repository.AccountHistoryRepository;
import com.service.kookchild.domain.management.repository.AccountRepository;
import com.service.kookchild.domain.mission.domain.Mission;
import com.service.kookchild.domain.mission.repository.MissionRepository;
import com.service.kookchild.domain.reward.dto.NotCompleteMissionDto;
import com.service.kookchild.domain.reward.dto.RewardInformationDTO;
import com.service.kookchild.domain.user.domain.ParentChild;
import com.service.kookchild.domain.user.domain.User;
import com.service.kookchild.domain.user.repository.ParentChildRepository;
import com.service.kookchild.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.omg.CORBA.UserException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final AccountHistoryRepository accountHistoryRepository;
    private final AccountRepository accountRepository;
    private final MissionRepository missionRepository;
    private final ParentChildRepository parentChildRepository;
    private final UserRepository userRepository;

    @Override
    public RewardInformationDTO rewardInformation(Long userId) {
        Account account = accountRepository.findAccountByType2AndUserId(userId).get();
        try {
            Long amount = accountHistoryRepository.rewardCompleteAmountLong(userId);
            account.setBalance(amount);
        }catch(Exception e){
            System.out.println(e);
        }

        return RewardInformationDTO.builder()
                .rewardCompleteAmount(accountHistoryRepository.rewardCompleteAmount(userId, account.getId()))
                .rewardConfirmWaitAmount(missionRepository.rewardConfirmWaitAmount(userId))
                .thisMonthInterestIncome(10L)
                .build();
    }

    @Override
    public NotCompleteMissionDto notCompleteMissions(Long id) {
       User u = userRepository.findById(id).get();

        ParentChild parentChild = parentChildRepository.findByChild(u);
        List<Mission> missions = missionRepository.findByParentChildIdAndChildConfirm(parentChild.getId(), false);
        List<String> list =
                missions.stream()
                        .map(Mission::getContent)
                        .collect(Collectors.toList());

        String amount = missionRepository.notCompletedMissionAmount(id);


        return NotCompleteMissionDto.builder()
                .missionContents(list)
                .notCompleteMissionsAmount(amount)
                .build();
    }

    @Override
    public void updateAccountHistoryIsWithdraw(Long id) {
        List<AccountHistory> list =
                accountHistoryRepository.findAccountHistoriesByCategoryAndUserId("리워드", id);

        Long amount = 0L;
        for(AccountHistory accountHistory: list){
            amount += accountHistory.getAmount();
        }

        List<AccountHistory> list2 =
                list
                        .stream()
                        .map(history -> {
                            history.setCategory("리워드출금");
                            return history;
                        })
                        .collect(Collectors.toList());

        accountHistoryRepository.saveAll(list2);

        Account account = accountRepository.findAccountByType2AndUserId(id).get();
        account.setBalance(0L);

        Account account1 = accountRepository.findAccountsByType1AndUserId(id).get();
        account1.setBalance(account1.getBalance()+amount);

//        accountRepository.save(account);
//        accountRepository.save(account1);
    }


}

