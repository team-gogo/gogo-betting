package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.result.persistence.BettingResultRepository
import gogo.gogobetting.domain.betting.root.application.dto.BettingInfoDto
import gogo.gogobetting.domain.betting.root.application.dto.BettingResultInfoDto
import gogo.gogobetting.domain.betting.root.application.dto.QueryBettingDto
import gogo.gogobetting.domain.betting.root.persistence.Betting
import org.springframework.stereotype.Component

@Component
class BettingMapper(
    private val bettingResultRepository: BettingResultRepository
) {

    fun map(betting: Betting?): QueryBettingDto {
        val isBetting = betting != null
        val result = betting?.let {
            bettingResultRepository.findByBettingIdAndIsCancelled(it.id, false)
        }

        return QueryBettingDto(
            isBetting = isBetting,
            betting = betting?.let {
                BettingInfoDto(
                    bettingId = it.id,
                    bettingPoint = it.point,
                    predictedWinTeamId = it.predictedWinTeamId
                )
            },
            result = result?.let {
                BettingResultInfoDto(
                    isPredicted = it.isPredicted,
                    earnedPoint = it.earnedPoint,
                )
            }
        )
    }

}
