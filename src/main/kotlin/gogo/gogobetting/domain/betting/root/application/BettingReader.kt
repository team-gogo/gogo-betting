package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import org.springframework.stereotype.Component

@Component
class BettingReader(
    private val bettingRepository: BettingRepository,
) {

    fun readBundleInfo(matchIds: List<Long>, studentId: Long): List<Betting> =
        bettingRepository.findBettingBundleInfo(matchIds, studentId)

    fun readBundleActiveInfo(matchIds: List<Long>, studentId: Long): List<Betting> =
        bettingRepository.findBettingBundleInfo(matchIds, studentId)

}
