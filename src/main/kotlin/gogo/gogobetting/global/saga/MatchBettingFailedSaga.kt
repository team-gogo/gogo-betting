package gogo.gogobetting.global.saga

import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.error.BettingException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MatchBettingFailedSaga(
    private val bettingRepository: BettingRepository,
) {

    @Transactional
    fun cancelledBatting(bettingId: Long) {
        val betting = bettingRepository.findByIdOrNull(bettingId)
            ?: throw BettingException(
                "Betting Not Found -- SAGA.cancelledBatting($bettingId)",
                HttpStatus.NOT_FOUND.value()
        )
        betting.cancel()
        bettingRepository.save(betting)
    }

}
