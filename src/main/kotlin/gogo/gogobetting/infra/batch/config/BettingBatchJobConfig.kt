package gogo.gogobetting.infra.batch.config

import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.infra.batch.listener.BatchExecutionListener
import gogo.gogobetting.infra.batch.service.BettingProcessor
import gogo.gogobetting.infra.batch.service.BettingReader
import gogo.gogobetting.infra.batch.service.BettingWriter
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
@EnableBatchProcessing
class BettingBatchJobConfig(
    private val bettingProcessor: BettingProcessor,
    private val bettingWriter: BettingWriter,
    private val batchExecutionListener: BatchExecutionListener,
    private val bettingReader: BettingReader,
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
            .chunk<Betting, BettingResult>(50)
            .transactionManager(transactionManager)
            .reader(bettingReader.bettingReader(null))
            .processor(bettingProcessor)
            .writer(bettingWriter)
            .build()
    }
}
