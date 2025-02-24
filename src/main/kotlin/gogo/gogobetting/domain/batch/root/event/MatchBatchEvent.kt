package gogo.gogobetting.domain.batch.root.event

data class MatchBatchEvent(
    val id: String,
    val matchId: Long,
    val victoryTeamId: Long,
    val aTeamScore: Int,
    val bTeamScore: Int,
    val students: List<StudentBettingDto>
)

data class StudentBettingDto(
    val studentId: Long,
    val earnedPoint: Long
)
