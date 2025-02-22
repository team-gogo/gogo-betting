package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.application.dto.BatchDto
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class BatchServiceImpl(
    private val jobLauncher: JobLauncher,
    private val job: Job
) : BatchService {

    @Transactional
    override fun batch(matchId: Long, dto: BatchDto) {

        // 중복 정산 valid
        // 해당 스테이지 관리자인지 valida
        // 배당률 계산 process

        val bettingOdds = 50.0

        val jobParameters = JobParametersBuilder()
            .addLong("matchId", matchId)
            .addLong("winTeamId", dto.winTeamId)
            .addLong("aTeamScore", dto.aTeamScore.toLong())
            .addLong("bTeamScore", dto.bTeamScore.toLong())
            .addDouble("bettingOdds", bettingOdds)
            .addDate("runDate", Date())
            .toJobParameters()

        jobLauncher.run(job, jobParameters)

    }

}
