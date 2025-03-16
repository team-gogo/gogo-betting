package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.domain.betting.root.application.dto.QueryBettingDto

interface BettingService {
    fun bet(matchId: Long, dto: BettingDto)
    fun query(matchId: Long, studentId: Long): QueryBettingDto
}
