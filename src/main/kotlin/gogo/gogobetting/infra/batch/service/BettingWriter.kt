package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.event.MatchBatchEvent
import gogo.gogobetting.domain.batch.root.event.StudentBettingDto
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.result.persistence.BettingResultRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.kafka.publisher.BatchPublisher
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.AfterStep
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
    private val batchPublisher: BatchPublisher,
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
    private val accumulatedItems = mutableListOf<BettingResult>()

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @BeforeStep
    fun beforeStep(stepExecution: StepExecution) {
        val jobExecution = stepExecution.jobExecution
        accumulatedItems.clear()
        batchId = jobExecution.executionContext["batchId"]!! as Long
    }

    @AfterStep
    fun afterStep(stepExecution: StepExecution) {
        val batch = batchRepository.findByIdOrNull(batchId)!!

        batchDetailRepository.save(
            BatchDetail.of(
                batchId = batch.id,
                victoryTeamId = winTeamId,
                aTeamScore = aTeamScore,
                bTeamScore = bTeamScore
            )
        )

        val successList = accumulatedItems.filter { it.isPredicted }
            .map {
                val studentId = bettingRepository.findByIdOrNull(it.bettingId)!!.studentId
                StudentBettingDto(studentId, it.earnedPoint)
            }

        val event = MatchBatchEvent(
            id = UUID.randomUUID().toString(),
            batchId = batchId,
            matchId = matchId,
            victoryTeamId = winTeamId,
            aTeamScore = aTeamScore,
            bTeamScore = bTeamScore,
            students = successList
        )

        log.info("published betting batch application event: {}", event.id)
        batchPublisher.publishBettingBatchEvent(event)
    }

    override fun write(items: Chunk<out BettingResult>) {
        bettingResultRepository.saveAll(items)
        accumulatedItems.addAll(items)
    }
}
