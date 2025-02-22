package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.application.dto.BatchDto
import gogo.gogobetting.global.util.UserContextUtil
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import java.util.*

@Service
class BatchServiceImpl(
    private val jobLauncher: JobLauncher,
    private val job: Job,
    private val batchReader: BatchReader,
    private val batchValidator: BatchValidator,
    private val userUtil: UserContextUtil
) : BatchService {

    override fun batch(matchId: Long, dto: BatchDto) {

        val studentId = userUtil.getCurrentStudent().studentId

        batchValidator.valid(matchId, studentId)

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

}
