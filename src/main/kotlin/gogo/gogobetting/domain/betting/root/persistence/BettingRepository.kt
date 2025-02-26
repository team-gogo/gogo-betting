package gogo.gogobetting.domain.betting.root.persistence

import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BettingRepository: JpaRepository<Betting, Long>, BettingCustomRepository {
    fun existsByMatchIdAndStudentIdAndStatus(matchId: Long, studentId: Long, status: BettingStatus): Boolean

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BettingResult br WHERE br.bettingId IN (:bettingIds)")
    fun deleteCancelledBatchResult(bettingIds: List<Long>)

    fun findAllByMatchId(matchId: Long): List<Betting>
}
