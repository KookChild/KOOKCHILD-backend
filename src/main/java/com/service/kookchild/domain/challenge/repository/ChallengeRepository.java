
package com.service.kookchild.domain.challenge.repository;

        import com.service.kookchild.domain.challenge.domain.Challenge;
        import com.service.kookchild.domain.challenge.domain.ChallengeState;
        import com.service.kookchild.domain.challenge.dto.ChallengeDTO;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;


        import java.time.LocalDate;
        import java.util.List;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    /* 0. User 조회 */

    /* 1. 전체 챌린지 조회 (진행중) */
//    @Query(value="SELECT * FROM Challenge c " +
//            "JOIN challenge_state cs ON c.id = cs.challenge_id " +
//            "JOIN parent_child p ON cs.parent_child_id = p.id " +
//            "WHERE p.child_id = ? " +
//            "AND c.start_date <= sysdate " +
//            "AND c.end_date >= sysdate " +
//            "AND cs.parent_confirm = 0 " +
//            "AND cs.child_confirm = 0 "
//           , nativeQuery = true)
//    List<Challenge> getAllChallenge(Long id);


    @Query(value = "SELECT * FROM Challenge c " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 FROM challenge_state cs " +
            "  WHERE c.id = cs.challenge_id " +
            "  AND cs.parent_child_id = ?" +
            ") " +
            "AND c.start_date <= sysdate " +
            "AND c.end_date >= sysdate", nativeQuery = true)
    List<Challenge> getAllChallenge(Long parentChildId);

    @Query(value = "SELECT DISTINCT c.* FROM Challenge c " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 FROM challenge_state cs " +
            "  WHERE c.id = cs.challenge_id " +
            "  AND cs.parent_child_id IN :parentChildIds" +
            ") " +
            "AND c.start_date <= sysdate " +
            "AND c.end_date >= sysdate", nativeQuery = true)
    List<Challenge> getAllChallengesByParentChildIdIn(@Param("parentChildIds") List<Long> parentChildIds);




    /* 2. 챌린지 내용 상세 조회 (나중에 frontend에서 child_content, parent_content 필터링 */
    Challenge findChallengeById(Long id);

    /* 3. (자녀가) 자신이 참여 중인 전체 챌린지 조회 (인자: child_id ) */

    @Query(value="SELECT  c.id, c.title, c.child_content, c.parent_content, c.image, c.bank_reward, c.start_date, c.end_date, cs.parent_reward FROM challenge c " +
            "JOIN challenge_state cs ON c.id = cs.challenge_id " +
            "JOIN parent_child p ON cs.parent_child_id = p.id " +
            "WHERE p.child_id = ? " +
            "AND c.start_date <= sysdate " +
            "AND c.end_date >= sysdate " +
            "AND cs.parent_confirm =1"+
            "AND cs.child_confirm=1"+
            "AND cs.is_success = 0",
            nativeQuery = true)
    List<Object[]> getChallengeListByChildId(Long childId);


    /* 4. (자녀가) 부모에게 추천받은 챌린지 조회 */

//    @Query(value = "SELECT * FROM Challenge c " +
//            "JOIN challenge_state cs ON c.id = cs.challenge_id " +
//            "JOIN parent_child p ON cs.parent_child_id = p.id " +
//            "WHERE p.child_id = ? " +
//            "AND c.start_date <= sysdate " +
//            "AND c.end_date >= sysdate " +
//            "AND cs.parent_confirm = 1 " +
//            "AND cs.child_confirm = 0 " +
//            "AND cs.is_success=0", nativeQuery = true)
//    List<Challenge> getRecommendedChallenges(Long childId);

    @Query(value = "SELECT c.id, c.title, c.child_content, c.parent_content, c.image, c.bank_reward, c.start_date, c.end_date, cs.parent_reward " +
            "FROM Challenge c " +
            "JOIN challenge_state cs ON c.id = cs.challenge_id " +
            "JOIN parent_child p ON cs.parent_child_id = p.id " +
            "WHERE p.child_id = ?1 " +
            "AND c.start_date <= sysdate " +
            "AND c.end_date >= sysdate " +
            "AND cs.parent_confirm = 1 " +
            "AND cs.child_confirm = 0 " +
            "AND cs.is_success=0", nativeQuery = true)
    List<Object[]> getRecommendedChallenges(Long childId);


    /* 챌린지 추천 */
//    @Query(value="")



}