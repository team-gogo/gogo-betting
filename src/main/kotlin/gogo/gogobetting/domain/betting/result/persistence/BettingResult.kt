package gogo.gogobetting.domain.betting.result.persistence

import gogo.gogobetting.domain.betting.root.persistence.Betting
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_betting_result")
class BettingResult(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @JoinColumn(name = "betting_id", nullable = false)
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    val betting: Betting,

    @Column(name = "is_predicted", nullable = false)
    val isPredicted: Boolean,

    @Column(name = "earned_point", nullable = false)
    val earnedPoint: Long

) {
    companion object {

        fun of(betting: Betting, earnedPoint: Long, isPredicted: Boolean) = BettingResult(
            betting = betting,
            isPredicted = predicted,
            earnedPoint = earnedPoint
        )

    }
}
