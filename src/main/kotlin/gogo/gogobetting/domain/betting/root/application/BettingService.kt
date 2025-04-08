package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingBundleDto
import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.domain.betting.root.application.dto.TotalBettingPointDto

interface BettingService {
    fun bet(matchId: Long, dto: BettingDto)
    fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto
    fun totalBettingPoint(matchIds: List<Long>, studentId: Long): TotalBettingPointDto
}
