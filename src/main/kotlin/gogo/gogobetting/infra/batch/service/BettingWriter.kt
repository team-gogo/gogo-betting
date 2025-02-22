package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.event.BettingBatchEvent
import gogo.gogobetting.domain.batch.root.event.StudentBettingDto
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.result.persistence.BettingResultRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*

@Component("springBatchBettingWriter")
@StepScope
class BettingWriter(
    private val bettingResultRepository: BettingResultRepository,
    private val batchDetailRepository: BatchDetailRepository,
    private val batchRepository: BatchRepository,
    private val bettingRepository: BettingRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : ItemWriter<BettingResult> {

    @Value("#{jobParameters['matchId']}")
    private val matchId: Long = 0

    @Value("#{jobParameters['winTeamId']}")
    private val winTeamId: Long = 0

    @Value("#{jobParameters['aTeamScore']}")
    private val aTeamScore: Int = 0

    @Value("#{jobParameters['bTeamScore']}")
    private val bTeamScore: Int = 0

    private var batchId: Long = 0

    @BeforeStep
    fun beforeStep(stepExecution: StepExecution) {
        val jobExecution = stepExecution.jobExecution
        batchId = jobExecution.executionContext["batchId"]!! as Long
    }

    override fun write(items: Chunk<out BettingResult>) {
        bettingResultRepository.saveAll(items)

        val batch = batchRepository.findByIdOrNull(batchId)!!
        batchDetailRepository.save(
            BatchDetail.of(
                batchId = batch.id,
                victoryTeamId = winTeamId,
                aTeamScore = aTeamScore,
                bTeamScore = bTeamScore
            )
        )

        val successList = items.filter { it.isPredicted }
            .map { StudentBettingDto(
                    bettingRepository.findByIdOrNull(it.bettingId)!!.id,
                    it.earnedPoint
                )
            }

        applicationEventPublisher.publishEvent(
            BettingBatchEvent(
                id = UUID.randomUUID().toString(),
                matchId = matchId,
                victoryTeamId = winTeamId,
                aTeamScore = aTeamScore,
                bTeamScore = bTeamScore,
                students = successList
            )
        )

    }
}
