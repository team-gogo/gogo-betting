package gogo.gogobetting.global.internal.stage.stub

import java.time.LocalDateTime

data class MatchApiInfo(
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val stage: StageApiInfoDto
)

data class StageApiInfoDto(
    val maintainers: List<Long>
)
