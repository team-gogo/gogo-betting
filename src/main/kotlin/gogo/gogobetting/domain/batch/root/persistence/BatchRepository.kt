package gogo.gogobetting.domain.batch.root.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface BatchRepository: JpaRepository<Batch, Long> {
    fun existsByMatchIdAndIsCancelledFalse(matchId: Long): Boolean
}
