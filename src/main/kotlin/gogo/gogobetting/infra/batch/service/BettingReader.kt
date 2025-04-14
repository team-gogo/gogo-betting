package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration("springBatchBettingReader")
class BettingReader(
    private val entityManagerFactory: EntityManagerFactory
) {

    @Bean("batchBettingReader")
    @StepScope
    fun bettingReader(
        @Value("#{jobParameters['matchId']}") matchId: Long?
    ): JpaPagingItemReader<Betting> {
        val validMatchId = matchId ?: throw IllegalArgumentException("matchId is required")
        return JpaPagingItemReader<Betting>().apply {
            setEntityManagerFactory(entityManagerFactory)
            setQueryString("SELECT b FROM Betting b WHERE b.matchId = :matchId AND b.status = :status ORDER BY b.id")
            setParameterValues(mapOf("matchId" to validMatchId, "status" to BettingStatus.CONFIRMED))
            pageSize = 50
        }
    }

}
