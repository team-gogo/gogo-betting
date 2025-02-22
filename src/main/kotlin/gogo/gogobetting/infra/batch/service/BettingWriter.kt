package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.event.BettingBatchEvent
import gogo.gogobetting.domain.batch.root.event.StudentBettingDto
import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.result.persistence.BettingResultRepository
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component("batchBettingWriter")
@StepScope
class BettingWriter(
    private val bettingResultRepository: BettingResultRepository,
    private val batchRepository: BatchRepository,
    private val batchDetailRepository: BatchDetailRepository,
) : ItemWriter<BettingResult> {

    lateinit var stepExecution: StepExecution

    @Value("#{jobParameters['matchId']}")
    private val matchId: Long = 0

    @Value("#{jobParameters['winTeamId']}")
    private val winTeamId: Long = 0

    @Value("#{jobParameters['aTeamScore']}")
    private val aTeamScore: Int = 0

    @Value("#{jobParameters['bTeamScore']}")
    private val bTeamScore: Int = 0

    override fun write(items: Chunk<out BettingResult>) {
        bettingResultRepository.saveAll(items)

        val batch = stepExecution.executionContext["batch"] as Batch

        batch.let {
            batchDetailRepository.save(
                BatchDetail.of(
                    batch = it,
                    victoryTeamId = winTeamId,
                    aTeamScore = aTeamScore,
                    bTeamScore = bTeamScore
                )
            )
        }

        val successList = items.filter { it.isPredicted }
            .map { StudentBettingDto(
                    it.betting.studentId,
                    it.earnedPoint
                )
            }

        val event = BettingBatchEvent(
            id = UUID.randomUUID().toString(),
            matchId = matchId,
            victoryTeamId = winTeamId,
            aTeamScore = aTeamScore,
            bTeamScore = bTeamScore,
            students = successList
        )

    }
}
