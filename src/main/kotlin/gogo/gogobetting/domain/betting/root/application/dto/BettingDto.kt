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
