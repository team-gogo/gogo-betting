package gogo.gogobetting.domain.batch.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface BatchRepository: JpaRepository<Batch, Long> {
    fun existsByMatchIdAndIsCancelledFalse(matchId: Long): Boolean

    fun findByMatchIdAndIsCancelledFalse(matchId: Long): Batch?

    fun findByMatchIdAndIsCancelledTrueOrderByCancelTimeDesc(matchId: Long): List<Batch>

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Batch b SET b.isCancelled = true, b.cancelTime = :now WHERE b.id = :batchId")
    fun cancelById(batchId: Long, now: LocalDateTime)
}
