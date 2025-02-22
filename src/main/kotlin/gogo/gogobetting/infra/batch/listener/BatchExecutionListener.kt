package gogo.gogobetting.infra.batch.listener

import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BatchExecutionListener(
    private val batchRepository: BatchRepository
) : JobExecutionListener {

    private lateinit var batch: Batch

    override fun beforeJob(jobExecution: JobExecution) {
        val matchId = jobExecution.jobParameters.getLong("matchId")
        if (matchId != null) {
            batch = batchRepository.save(
                Batch.of(
                    matchId = matchId,
                    studentId = 0L,
                    startTime = LocalDateTime.now(),
                    endTime = null
                )
            )
            jobExecution.executionContext.put("batch", batch)
        }
    }

    override fun afterJob(jobExecution: JobExecution) {
        val batch = jobExecution.executionContext["batch"] as Batch
        batch.updateEndTime()
        batchRepository.save(batch)
    }
}
