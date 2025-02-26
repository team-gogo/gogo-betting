package gogo.gogobetting.domain.batch.root.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_batch")
class Batch(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long = 0,

    @Column(name = "match_id", nullable = false)
    val matchId: Long,

    @Column(name = "student_id", nullable = false)
    val studentId: Long,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = true)
    var endTime: LocalDateTime?,

    @Column(name = "is_cancelled", nullable = false)
    var isCancelled: Boolean,

    @Column(name = "cancel_time", nullable = true)
    var cancelTime: LocalDateTime? = null,

) {

    fun updateEndTime() {
        this.endTime = LocalDateTime.now()
    }

    fun cancel() {
        this.isCancelled = true
        this.cancelTime = LocalDateTime.now()
    }

    companion object {

        fun of(matchId: Long, studentId: Long, startTime: LocalDateTime, endTime: LocalDateTime?) = Batch(
            matchId = matchId,
            studentId = studentId,
            startTime = startTime,
            endTime = endTime,
            isCancelled = false,
        )

    }
}
