package gogo.gogobetting.global.internal.stage.api

import gogo.gogobetting.global.feign.client.StageClient
import gogo.gogobetting.global.internal.stage.stub.IsMaintainerDto
import org.springframework.stereotype.Component

@Component
class StageApiImpl(
    private val stageClient: StageClient
) : StageApi {
    override fun isMaintainer(matchLId: Long, studentId: Long): IsMaintainerDto =
        stageClient.isMaintainer(matchLId, studentId)
}
