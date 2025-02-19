package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.global.util.UserContextUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BettingServiceImpl(
    private val userUtil: UserContextUtil,
    private val bettingValidator: BettingValidator,
) : BettingService {

    @Transactional
    override fun bet (matchId: Long, dto: BettingDto) {
        // 동시성 처리 필요
        val student = userUtil.getCurrentStudent()
        bettingValidator.valid(matchId, student.studentId)
    }

}
