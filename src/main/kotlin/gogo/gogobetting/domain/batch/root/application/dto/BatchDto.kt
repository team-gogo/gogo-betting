package gogo.gogobetting.domain.batch.root.application.dto

import jakarta.validation.constraints.NotNull

data class BatchDto(
    @NotNull
    val winTeamId: Long,
    @NotNull
    val aTeamScore: Int,
    @NotNull
    val bTeamScore: Int
)
