package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingDto

interface BettingService {
    fun bet(matchId: Long, dto: BettingDto)
}
