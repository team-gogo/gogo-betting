package gogo.gogobetting.domain.betting.root.persistence

import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BettingRepository: JpaRepository<Betting, Long>, BettingCustomRepository {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun existsByMatchIdAndStudentIdAndStatus(matchId: Long, studentId: Long, status: BettingStatus): Boolean

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE BettingResult br SET br.isCancelled = true WHERE br.bettingId IN (:bettingIds)")
    fun cancelledBatchResult(bettingIds: List<Long>)

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE BettingResult br SET br.isCancelled = false WHERE br.bettingId IN (:bettingIds)")
    fun rollbackCancelledBatchResult(bettingIds: List<Long>)

    fun findAllByMatchId(matchId: Long): List<Betting>

    fun countByMatchId(matchId: Long): Long
}
