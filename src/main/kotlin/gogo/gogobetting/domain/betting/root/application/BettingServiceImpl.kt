package gogo.gogobetting.domain.betting.root.application

import gogo.gogobetting.domain.betting.root.application.dto.BettingBundleDto
import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.domain.betting.root.event.MatchBettingEvent
import gogo.gogobetting.global.util.UserContextUtil
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BettingServiceImpl(
    private val userUtil: UserContextUtil,
    private val bettingValidator: BettingValidator,
    private val bettingProcessor: BettingProcessor,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val bettingReader: BettingReader,
    private val bettingMapper: BettingMapper,
) : BettingService {

    @Transactional
    override fun bet (matchId: Long, dto: BettingDto) {
        val student = userUtil.getCurrentStudent()
        bettingValidator.valid(matchId, student.studentId)
        val betting = bettingProcessor.save(matchId, student.studentId, dto)

        applicationEventPublisher.publishEvent(
            MatchBettingEvent(
                id = UUID.randomUUID().toString(),
                studentId = student.studentId,
                bettingId = betting.id,
                matchId = matchId,
                predictedWinTeamId = dto.predictedWinTeamId,
                bettingPoint = dto.bettingPoint
            )
        )
    }

    @Transactional(readOnly = true)
    override fun bundle(matchIds: List<Long>, studentId: Long): BettingBundleDto {
        val bettings = bettingReader.readBundleInfo(matchIds, studentId)
        return bettingMapper.mapBundle(bettings)
    }

}
