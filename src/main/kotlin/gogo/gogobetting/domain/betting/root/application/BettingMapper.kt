package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.result.persistence.BettingResultRepository
import gogo.gogobetting.domain.betting.root.application.dto.*
import gogo.gogobetting.domain.betting.root.persistence.Betting
import org.springframework.stereotype.Component

@Component
class BettingMapper(
    private val bettingResultRepository: BettingResultRepository)
{

    fun mapBundle(bettingList: List<Betting>): BettingBundleDto {
        val bundleInfo = bettingList.map { betting ->
            val bettingInfoDto = BettingInfoDto(betting.id, betting.point, betting.predictedWinTeamId)

            val bettingResult = bettingResultRepository.findByBettingIdAndIsCancelled(betting.id)
            val bettingResultInfoDto = bettingResult?.let {
                    BettingResultInfoDto(
                        isPredicted =bettingResult.isPredicted,
                        earnedPoint =   bettingResult.earnedPoint
                    )
            }

            BettingBundleInfoDto(
                betting.matchId,
                bettingInfoDto,
                bettingResultInfoDto
            )
        }

        return BettingBundleDto(bundleInfo)
    }

    fun mapBettingPoint(bettingList: List<Betting>): TotalBettingPointDto =
        TotalBettingPointDto(
            bettingPoint = bettingList.sumOf { it.point }
        )

}
