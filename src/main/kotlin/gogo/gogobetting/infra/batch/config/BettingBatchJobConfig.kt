package gogo.gogobetting.infra.batch.config

import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.infra.batch.listener.BatchExecutionListener
import gogo.gogobetting.infra.batch.service.BettingProcessor
import gogo.gogobetting.infra.batch.service.BettingWriter
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.batch.core.StepExecution

@Configuration
@EnableBatchProcessing
class BettingBatchJobConfig(
    private val bettingRepository: BettingRepository,
    private val bettingProcessor: BettingProcessor,
    private val bettingWriter: BettingWriter,
    private val batchExecutionListener: BatchExecutionListener
) {

    @Bean
    fun bettingJob(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Job {
        return JobBuilder("bettingJob", jobRepository)
            .listener(batchExecutionListener)
            .start(bettingStep(jobRepository, transactionManager))
            .build()
    }

    @Bean
    fun bettingStep(
        jobRepository: JobRepository,
        transactionManager: PlatformTransactionManager,
    ): Step {
        return StepBuilder("bettingStep", jobRepository)
            .chunk<Betting, BettingResult>(50)
            .reader(bettingReader(null))
            .processor(bettingProcessor)
            .writer(bettingWriter)
            .transactionManager(transactionManager)
            .build()
    }

    @Bean
    @StepScope
    fun bettingReader(
        @Value("#{jobParameters['matchId']}") matchId: Long?
    ): ItemReader<Betting> {
        val validMatchId = matchId ?: throw IllegalArgumentException("matchId is required")
        return ItemReader {
            val bettingIterator = bettingRepository.findByMatchId(validMatchId).iterator()
            if (bettingIterator.hasNext()) bettingIterator.next() else null
        }
    }
}
