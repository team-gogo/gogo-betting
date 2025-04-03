package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.application.dto.BatchDto
import gogo.gogobetting.domain.batch.root.event.MatchBatchEvent
import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
class BatchProcessor(
    private val batchRepository: BatchRepository,
    private val bettingRepository: BettingRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val batchDetailRepository: BatchDetailRepository,
) {

    @Transactional
    fun emptyBettingBatch(matchId: Long, dto: BatchDto, studentId: Long, isEmptyBetting: Boolean) {
        val batch = Batch.of(
            matchId = matchId,
            studentId = studentId,
            startTime = LocalDateTime.now(),
            endTime = null,
        )
        batchRepository.save(batch)

        val batchDetail = BatchDetail.of(
            batchId = batch.id,
            victoryTeamId = dto.winTeamId,
            aTeamScore = dto.aTeamScore,
            bTeamScore = dto.bTeamScore,
        )
        batchDetailRepository.save(batchDetail)

        batch.updateEndTime()
        batchRepository.save(batch)

        applicationEventPublisher.publishEvent(
            MatchBatchEvent(
                id = UUID.randomUUID().toString(),
                batchId = batch.id,
                matchId = matchId,
                victoryTeamId = dto.winTeamId,
                aTeamScore = dto.aTeamScore,
                bTeamScore = dto.bTeamScore,
                students = emptyList(),
            )
        )
    }

    fun cancel(batch: Batch) {
        val cancelBettingIds = bettingRepository.findAllByMatchId(batch.matchId)
        bettingRepository.cancelledBettingResult(cancelBettingIds.map { it.id })
        val now = LocalDateTime.now()
        batchRepository.cancelById(batch.id, now)
    }

}
