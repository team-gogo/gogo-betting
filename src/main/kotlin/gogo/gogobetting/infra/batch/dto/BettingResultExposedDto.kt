package gogo.gogobetting.infra.batch.dto

data class BettingResultExposedDto(
    val bettingId: Long,
    val earnedPoint: Long,
    val isPredicted: Boolean,
)
