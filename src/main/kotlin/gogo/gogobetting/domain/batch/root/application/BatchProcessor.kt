package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.stereotype.Component

@Component
class BatchProcessor(
    private val batchRepository: BatchRepository,
    private val bettingRepository: BettingRepository
) {

    fun cancel(batch: Batch) {
        batch.cancel()
        batchRepository.save(batch)
        val cancelBettingIds = bettingRepository.findAllByMatchId(batch.matchId)
        bettingRepository.cancelledBatchResult(cancelBettingIds.map { it.id })
    }

}
