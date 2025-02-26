package gogo.gogobetting.domain.betting.result.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BettingResultRepository: JpaRepository<BettingResult, Long> {
}
