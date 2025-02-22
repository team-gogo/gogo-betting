package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.stereotype.Component

@Component
class BatchReader(
    private val bettingRepository: BettingRepository,
) {

    fun readBettingOdds(matchId: Long, winTeamId: Long): Double =
        bettingRepository.calcOdds(matchId, winTeamId).odds

}
