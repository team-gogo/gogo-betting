package gogo.gogobetting.domain.betting.root.event

data class MatchBettingEvent(
    val id: String,
    val studentId: Long,
    val bettingId: Long,
    val matchId: Long,
    val predictedWinTeamId: Long,
    val bettingPoint: Long,
)
