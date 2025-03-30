package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.application.dto.BatchDto
import gogo.gogobetting.domain.batch.root.event.BatchCancelEvent
import gogo.gogobetting.global.util.UserContextUtil
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BatchServiceImpl(
    private val jobLauncher: JobLauncher,
    private val job: Job,
    private val batchReader: BatchReader,
    private val batchProcessor: BatchProcessor,
    private val batchValidator: BatchValidator,
    private val userUtil: UserContextUtil,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : BatchService {

    override fun batch(matchId: Long, dto: BatchDto) {
        val studentId = userUtil.getCurrentStudent().studentId

        val isEmptyBetting = batchValidator.valid(matchId, studentId)

        if (isEmptyBetting) {
            batchProcessor.emptyBettingBatch(matchId, dto, studentId, isEmptyBetting)
            return
        }

        val bettingOdds = batchReader.readBettingOdds(matchId, dto.winTeamId)

        val jobParameters = JobParametersBuilder()
            .addLong("matchId", matchId)
            .addLong("studentId", studentId)
            .addLong("winTeamId", dto.winTeamId)
            .addLong("aTeamScore", dto.aTeamScore.toLong())
            .addLong("bTeamScore", dto.bTeamScore.toLong())
            .addDouble("bettingOdds", bettingOdds)
            .addDate("runDate", Date())
            .toJobParameters()

        jobLauncher.run(job, jobParameters)

    }

    @Transactional
    override fun cancel(matchId: Long) {
        val studentId = userUtil.getCurrentStudent().studentId
        val batch = batchReader.readByMatchIdForWrite(matchId)
        batchValidator.cancelValid(batch, matchId, studentId)
        batchProcessor.cancel(batch)

        applicationEventPublisher.publishEvent(
            BatchCancelEvent(
                id = UUID.randomUUID().toString(),
                batchId = batch.id,
                matchId = matchId,
            )
        )
    }

}
