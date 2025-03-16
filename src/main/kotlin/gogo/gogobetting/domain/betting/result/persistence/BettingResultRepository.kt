package gogo.gogobetting.domain.betting.result.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BettingResultRepository: JpaRepository<BettingResult, Long> {
    fun findByBettingIdAndIsCancelled(bettingId: Long, isCancelled: Boolean = false): BettingResult?
}
