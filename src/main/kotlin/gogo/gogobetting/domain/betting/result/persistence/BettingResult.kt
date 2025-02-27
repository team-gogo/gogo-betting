package gogo.gogobetting.domain.betting.result.persistence

import jakarta.persistence.*

@Entity
@Table(name = "tbl_betting_result")
class BettingResult(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @JoinColumn(name = "betting_id", nullable = false)
    val bettingId: Long,

    @Column(name = "is_predicted", nullable = false)
    val isPredicted: Boolean,

    @Column(name = "earned_point", nullable = false)
    val earnedPoint: Long,

    @Column(name = "is_cancelled", nullable = false)
    var isCancelled: Boolean = false,

) {
    
    fun cancel() {
        isCancelled = true
    }

    companion object {

        fun of(bettingId: Long, earnedPoint: Long, isPredicted: Boolean) = BettingResult(
            bettingId = bettingId,
            isPredicted = isPredicted,
            earnedPoint = earnedPoint
        )

    }
}
