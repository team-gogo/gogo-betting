package gogo.gogobetting.infra.batch.dto

data class BettingResultJdbcDto(
    val bettingId: Long,
    val earnedPoint: Long,
    val isPredicted: Boolean,
)
