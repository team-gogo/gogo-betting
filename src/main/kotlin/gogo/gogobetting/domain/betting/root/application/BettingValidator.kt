package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus
import gogo.gogobetting.global.error.BettingException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class BettingValidator(
    private val bettingRepository: BettingRepository
) {

    fun valid(matchId: Long, studentId: Long) {
        val isBet =
            bettingRepository.existsByMatchIdAndStudentIdAndStatus(matchId, studentId, BettingStatus.CONFIRMED)

        if (isBet) {
        }
    }

}
