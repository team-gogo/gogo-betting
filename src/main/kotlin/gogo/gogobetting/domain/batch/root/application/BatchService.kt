package gogo.gogobetting.domain.batch.root.application

import gogo.gogobetting.domain.batch.root.application.dto.BatchDto

interface BatchService {
    fun batch(matchId: Long, dto: BatchDto)
}
