package gogo.gogobetting.infra.batch.dto

data class BettingRow(
    val id: Long,
    val matchId: Long,
    val studentId: Long,
    val point: Long,
    val predictedWinTeamId: Long,
)
