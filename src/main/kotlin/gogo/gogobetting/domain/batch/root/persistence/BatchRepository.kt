package gogo.gogobetting.domain.batch.root.persistence

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface BatchRepository: JpaRepository<Batch, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun existsByMatchIdAndIsCancelledFalse(matchId: Long): Boolean

    fun findByMatchIdAndIsCancelledFalse(matchId: Long): Batch?

    fun findByMatchIdAndIsCancelledTrueOrderByCancelTimeDesc(matchId: Long): List<Batch>

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Batch b SET b.isCancelled = true, b.cancelTime = :now WHERE b.id = :batchId")
    fun cancelById(batchId: Long, now: LocalDateTime)

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Batch b SET b.isCancelled = false, b.cancelTime = null WHERE b.id = :batchId")
    fun rollBackCancelledById(batchId: Long)
}
