package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.event.MatchBatchEvent
import gogo.gogobetting.domain.batch.root.event.StudentBettingDto
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.result.persistence.BettingResultTable
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.kafka.publisher.BatchPublisher
import gogo.gogobetting.infra.batch.dto.BettingResultExposedDto
import gogo.gogobetting.infra.batch.dto.BettingRow
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.util.*
import javax.sql.DataSource
import kotlin.math.ceil

@Component("springBatchBettingWriter")
@StepScope
class BettingResultWriter(
    private val batchDetailRepository: BatchDetailRepository,
    private val batchRepository: BatchRepository,
    private val bettingRepository: BettingRepository,
    private val batchPublisher: BatchPublisher,
    @Value("#{jobParameters['winTeamId']}") private val winTeamId: Long,
    @Value("#{jobParameters['bettingOdds']}") private val bettingOdds: Double,
    @Value("#{jobParameters['matchId']}") private val matchId: Long,
    @Value("#{jobParameters['aTeamScore']}") private val aTeamScore: Int,
    @Value("#{jobParameters['bTeamScore']}") private val bTeamScore: Int,
    private val dataSource: DataSource
) : ItemWriter<BettingRow> {

    private var batchId: Long = 0
    private val accumulated = mutableListOf<BettingResultExposedDto>()
    private val log = LoggerFactory.getLogger(this::class.java)

    @BeforeStep
    fun beforeStep(stepExecution: StepExecution) {
        batchId = stepExecution.jobExecution.executionContext["batchId"] as Long
        accumulated.clear()
    }

    override fun write(items: Chunk<out BettingRow>) {
        val dtoList = items.map { row ->
            val isPredicted = row.predictedWinTeamId == winTeamId
            val earnedPoint = if (isPredicted) ceil(row.point * bettingOdds).toLong() + row.point else 0L
            BettingResultExposedDto(
                bettingId = row.id,
                earnedPoint = earnedPoint,
                isPredicted = isPredicted,
            )
        }

        Database.connect(dataSource)
        transaction {
            BettingResultTable.batchInsert(dtoList) { item ->
                this[BettingResultTable.bettingId] = item.bettingId
                this[BettingResultTable.earnedPoint] = item.earnedPoint
                this[BettingResultTable.isCancelled] = false
                this[BettingResultTable.isPredicted] = item.isPredicted
            }
        }

        accumulated.addAll(dtoList)
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

        val successList = accumulated.filter { it.isPredicted }
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
}
