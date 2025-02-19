package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.stereotype.Component

@Component
class BettingProcessor(
    private val bettingRepository: BettingRepository
) {

    fun save(matchId: Long, studentId: Long, dto: BettingDto) {
        val betting = Betting.of(
            matchId = matchId,
            studentId = studentId,
            point = dto.bettingPoint,
            predictedWinTeamId = dto.predictedWinTeamId,
        )

        bettingRepository.save(betting)
    }

}
