package gogo.gogobetting.domain.betting.root.persistence

import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_betting")
class Betting(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "match_id", nullable = false)
    val matchId: Long,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "point", nullable = false)
    val point: Long,

    @Column(name = "predicted_win_team_id", nullable = false)
    val predictedWinTeamId: Long,

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: BettingStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

) {

    fun cancel() {
        status = BettingStatus.CANCELLED
    }

    companion object {

        fun of(matchId: Long, studentId: Long, point: Long, predictedWinTeamId: Long) = Betting(
            matchId = matchId,
            studentId = studentId,
            point = point,
            predictedWinTeamId = predictedWinTeamId,
            status = BettingStatus.CONFIRMED,
        )

    }
}
