package gogo.gogobetting.domain.batch.root.application.dto

data class BatchDto(
    val winTeamId: Long,
    val aTeamScore: Int,
    val bTeamScore: Int
)
