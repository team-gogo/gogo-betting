package gogo.gogobetting.domain.betting.root.persistence

import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import org.springframework.data.jpa.repository.JpaRepository

interface BettingRepository: JpaRepository<Betting, Long> {
    fun existsByMatchIdAndStudentIdAndStatus(matchId: Long, studentId: Long, status: BettingStatus): Boolean
}
