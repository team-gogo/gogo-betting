package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.domain.betting.result.persistence.BettingResult
import gogo.gogobetting.domain.betting.root.persistence.Betting
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import kotlin.math.ceil

@Component("batchBettingProcessor")
@StepScope
class BettingProcessor : ItemProcessor<Betting, BettingResult> {

    @Value("#{jobParameters['winTeamId']}")
    private val winTeamId: Long = 0

    @Value("#{jobParameters['bettingOdds']}")
    private val bettingOdds: Double = 0.0

    override fun process(betting: Betting): BettingResult {
        val isPredicted = betting.predictedWinTeamId == winTeamId
        val earnedPoint = if (isPredicted) ceil(betting.point * bettingOdds).toLong() + betting.point else 0

        val bettingResult = BettingResult.of(
            bettingId = betting.id,
            isPredicted = isPredicted,
            earnedPoint = earnedPoint
        )

        return bettingResult
    }

}
