package gogo.gogobetting.domain.betting.root.application.dto

import jakarta.validation.constraints.NotNull

data class BettingDto(
    @NotNull
    val predictedWinTeamId: Long,
    @NotNull
    val bettingPoint: Long
)

data class MatchOddsDto(
    val odds: Double
)

data class QueryBettingDto(
    val isBetting: Boolean,
    val betting: BettingInfoDto?,
    val result: BettingResultInfoDto?
)

data class BettingInfoDto(
    val bettingId: Long,
    val bettingPoint: Long,
    val predictedWinTeamId: Long,
)

data class BettingResultInfoDto(
    val isPredicted: Boolean,
    val earnedPoint: Long,
)

