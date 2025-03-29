package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.error.BettingException
import gogo.gogobetting.global.internal.stage.api.StageApi
import gogo.gogobetting.global.internal.stage.stub.MatchApiInfo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class BatchValidator(
    private val batchRepository: BatchRepository,
    private val stageApi: StageApi,
    private val bettingRepository: BettingRepository
) {

    @Transactional
    fun valid(matchId: Long, studentId: Long): Boolean {
        val bettingCount = bettingRepository.countByMatchId(matchId)

        val isDuplicate = batchRepository.existsByMatchIdAndIsCancelledFalse(matchId)
        if (isDuplicate) {
            throw BettingException("Duplicate Batch, Match Id: $matchId", HttpStatus.BAD_REQUEST.value())
        }

        val cancelBatch = batchRepository.findByMatchIdAndIsCancelledTrueOrderByCancelTimeDesc(matchId).firstOrNull()
        val now = LocalDateTime.now()
        val waitMinute = 3L
        if (
            cancelBatch != null &&
            now.isBefore(cancelBatch.cancelTime!!.plusMinutes(waitMinute))
        ) {
            throw BettingException(
                "정산이 취소되었다면 ${waitMinute}분 후에 다시 정산이 가능합니다. " +
                        "Match Id: $matchId, " +
                        "남은 시간: ${cancelBatch.cancelTime!!.plusMinutes(waitMinute).minute - now.minute}분"
                ,HttpStatus.FORBIDDEN.value())
        }

        val matchDto = stageApi.matchApiInfo(matchId)
        isMaintainer(matchDto, studentId, matchId)

        if (LocalDateTime.now().isBefore(matchDto.endDate)) {
            throw BettingException("Match Is Not End, Match Id: $matchId", HttpStatus.BAD_REQUEST.value())
        }

        return bettingCount == 0L
    }

    fun cancelValid(batch: Batch, matchId: Long, studentId: Long) {
        val now = LocalDateTime.now()
        if (now.isAfter(batch.endTime!!.plusMinutes(5))) {
            throw BettingException("정산 이후 5분이 지난 후에는 정산 취소가 불가능합니다.", HttpStatus.FORBIDDEN.value())
        }

        val matchDto = stageApi.matchApiInfo(matchId)
        isMaintainer(matchDto, studentId, matchId)

        return
    }

    private fun isMaintainer(
        matchDto: MatchApiInfo,
        studentId: Long,
        matchId: Long
    ) {
        val isMaintainer = matchDto.stage.maintainers
            .any { id -> id == studentId }
        if (isMaintainer.not()) {
            throw BettingException(
                "Not Maintainer, Student Id: $studentId, Match Id: $matchId",
                HttpStatus.FORBIDDEN.value()
            )
        }
    }

}
