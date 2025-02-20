package gogo.gogobetting.domain.batch.detail.persistence

import gogo.gogobetting.domain.batch.root.persistence.Batch
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_batch_detail")
class BatchDetail(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @JoinColumn(name = "batch_id", nullable = false)
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    val batch: Batch,

    @JoinColumn(name = "victory_team_id", nullable = false)
    val victoryTeamId: Long,

    @JoinColumn(name = "a_team_score", nullable = false)
    val aTeamScore: Int,

    @JoinColumn(name = "b_team_score", nullable = false)
    val bTeamScore: Int,

) {
    companion object {

        fun of(batch: Batch, victoryTeamId: Long, aTeamScore: Int, bTeamScore: Int) = BatchDetail(
            batch = batch,
            victoryTeamId = victoryTeamId,
            aTeamScore = aTeamScore,
            bTeamScore = bTeamScore
        )

    }
}
