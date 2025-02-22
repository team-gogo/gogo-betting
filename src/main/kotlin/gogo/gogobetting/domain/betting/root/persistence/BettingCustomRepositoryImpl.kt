package gogo.gogobetting.domain.betting.root.persistence

import com.querydsl.jpa.impl.JPAQueryFactory
import gogo.gogobetting.domain.betting.root.application.dto.MatchOddsDto
import gogo.gogobetting.domain.betting.root.persistence.QBetting.betting
import gogo.gogobetting.domain.betting.root.persistence.type.BettingStatus.*
import org.springframework.stereotype.Repository

@Repository
class BettingCustomRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BettingCustomRepository {

    override fun calcOdds(matchId: Long, winTeamId: Long): MatchOddsDto {
        val winTeamPoint = queryFactory
            .select(betting.point.sum())
            .from(betting)
            .where(
                betting.matchId.eq(matchId),
                betting.predictedWinTeamId.eq(winTeamId),
                betting.status.eq(CONFIRMED)
            )
            .fetchOne() ?: 0L

        val loseTeamPoint = queryFactory
            .select(betting.point.sum())
            .from(betting)
            .where(
                betting.matchId.eq(matchId),
                betting.predictedWinTeamId.ne(winTeamId),
                betting.status.eq(CONFIRMED)
            )
            .fetchOne() ?: 0L

        val odds = if (winTeamPoint == 0L) 0.0 else loseTeamPoint.toDouble() / winTeamPoint.toDouble()

        return MatchOddsDto(odds)
    }

}
