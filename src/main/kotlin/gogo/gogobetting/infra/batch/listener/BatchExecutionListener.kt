package gogo.gogobetting.infra.batch.listener

import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BatchExecutionListener(
    private val batchRepository: BatchRepository
) : JobExecutionListener {

    private lateinit var batch: Batch

    override fun beforeJob(jobExecution: JobExecution) {
        val matchId = jobExecution.jobParameters.getLong("matchId")!!
        val studentId = jobExecution.jobParameters.getLong("studentId")!!
        batch = batchRepository.save(
            Batch.of(
                matchId = matchId,
                studentId = studentId,
                startTime = LocalDateTime.now(),
                endTime = null
            )
        )
        jobExecution.executionContext.put("batchId", batch.id)
    }

    override fun afterJob(jobExecution: JobExecution) {
        val batchId = jobExecution.executionContext.getLong("batchId")
        val batch = batchRepository.findByIdOrNull(batchId)!!
        batch.updateEndTime()
        if (jobExecution.status.isUnsuccessful) {
            batch.failed()
        }
        batchRepository.save(batch)
    }
}