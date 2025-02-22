package gogo.gogobetting.domain.betting.root.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_betting")
class Betting(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long,

    @Column(name = "game_id", nullable = false)
    val gameId: Long,

    @Column(name = "match_id", nullable = false)
    val matchId: Long,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "point", nullable = false)
    val point: Long,

    @Column(name = "predicted_win_team_id", nullable = false)
    val predictedWinTeamId: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

)
