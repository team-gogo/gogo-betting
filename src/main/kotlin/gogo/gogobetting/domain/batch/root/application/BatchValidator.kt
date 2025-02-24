package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.global.error.BettingException
import gogo.gogobetting.global.internal.stage.api.StageApi
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BatchValidator(
    private val batchRepository: BatchRepository,
    private val stageApi: StageApi
) {

    fun valid(matchId: Long, studentId: Long) {
        val matchDto = stageApi.matchApiInfo(matchId)

        if (LocalDateTime.now().isBefore(matchDto.endDate)) {
            throw BettingException("Match Is Not End, Match Id: $matchId", HttpStatus.BAD_REQUEST.value())
        }

        val isDuplicate = batchRepository.existsByMatchIdAndIsCancelledFalse(matchId)
        if (isDuplicate) {
            throw BettingException("Duplicate Batch, Match Id: $matchId", HttpStatus.BAD_REQUEST.value())
        }

        val isMaintainer = matchDto.stage.maintainers
            .any { id -> id == studentId }
        if (isMaintainer.not()) {
            throw BettingException("Not Maintainer, Student Id: $studentId, Match Id: $matchId", HttpStatus.BAD_REQUEST.value())
        }
    }

}
