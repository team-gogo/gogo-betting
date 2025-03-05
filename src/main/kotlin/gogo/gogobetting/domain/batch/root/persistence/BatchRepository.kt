package gogo.gogobetting.domain.batch.root.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface BatchRepository: JpaRepository<Batch, Long> {
    fun existsByMatchIdAndIsCancelledFalse(matchId: Long): Boolean

    fun findByMatchIdAndIsCancelledFalse(matchId: Long): Batch?

    fun findByMatchIdAndIsCancelledTrueOrderByCancelTimeDesc(matchId: Long): List<Batch>

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM BatchDetail bd WHERE bd.batchId = :batchId")
    fun deleteCancelledBatchDetail(batchId: Long)
}
