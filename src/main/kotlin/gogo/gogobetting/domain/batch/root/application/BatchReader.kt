package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.error.BettingException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class BatchReader(
    private val bettingRepository: BettingRepository,
    private val batchRepository: BatchRepository,
) {

    fun readBettingOdds(matchId: Long, winTeamId: Long): Double =
        bettingRepository.calcOdds(matchId, winTeamId).odds

    fun readByMatchIdForWrite(matchId: Long) = batchRepository.findByMatchIdAndIsCancelledFalse(matchId)
        ?: throw BettingException("Not Found Batch, Match Id: $matchId", HttpStatus.NOT_FOUND.value())

}
