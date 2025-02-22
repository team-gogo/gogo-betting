package gogo.gogobetting.domain.batch.root.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tbl_batch")
class Batch(

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

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = false)
    val endTime: LocalDateTime,

    @Column(name = "is_cancelled", nullable = false)
    val isCancelled: Boolean,

)
