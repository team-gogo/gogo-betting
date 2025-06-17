package gogo.gogobetting.infra.batch.config

import gogo.gogobetting.infra.batch.service.BettingResultWriter
import gogo.gogobetting.infra.batch.service.BettingReader
import gogo.gogobetting.infra.batch.dto.BettingRow
import gogo.gogobetting.infra.batch.listener.BatchExecutionListener
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
@EnableBatchProcessing
class BettingBatchJobConfig(
    private val bettingResultWriter: BettingResultWriter,
    private val batchExecutionListener: BatchExecutionListener,
    private val bettingReader: BettingReader,
    private val dateSource: DataSource,
) {

    @Bean
    fun bettingJob(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Job {
        return JobBuilder("bettingJob", jobRepository)
            .start(bettingStep(jobRepository, transactionManager))
            .listener(batchExecutionListener)
            .build()
    }

    @Bean
    fun bettingStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Step {
        return StepBuilder("bettingStep", jobRepository)
            .chunk<BettingRow, BettingRow>(1000)
            .transactionManager(transactionManager)
            .reader(bettingReader.jdbcBettingReader(null, dateSource))
            .writer(bettingResultWriter)
            .build()
    }
}
