package gogo.gogobetting.global.saga

import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.error.BettingException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AdditionTempPointFailedSaga(
    private val bettingRepository: BettingRepository,
    private val batchRepository: BatchRepository,
) {

    @Transactional
    fun cancelledBatch(batchId: Long) {
        val batch = (batchRepository.findByIdOrNull(batchId)
            ?: throw BettingException("Batch Not Found -- SAGA.cancelledBatch($batchId)", HttpStatus.NOT_FOUND.value()))
        batch.cancel()
        batchRepository.save(batch)

        val bettings = bettingRepository.findAllByMatchId(batch.matchId)
        bettingRepository.cancelledBatchResult(bettings.map { it.id })
    }

}
