package gogo.gogobetting.domain.betting.root.persistence

import gogo.gogobetting.domain.betting.root.application.dto.BettingBundleInfoDto
import gogo.gogobetting.domain.betting.root.application.dto.MatchOddsDto

interface BettingCustomRepository {
    fun calcOdds(matchId: Long, winTeamId: Long): MatchOddsDto
    fun findBettingBundleInfo(matchIds: List<Long>, studentId: Long): List<BettingBundleInfoDto>
}
