package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.stereotype.Component

@Component
class BettingReader(
    private val bettingRepository: BettingRepository,
) {

    fun readOrNull(matchId: Long, studentId: Long): Betting? =
        bettingRepository.findByMatchIdAndStudentIdAndStatus(matchId, studentId)

}
