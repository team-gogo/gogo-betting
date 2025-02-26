package gogo.gogobetting.global.internal.stage.api

import gogo.gogobetting.global.feign.client.StageClient
import gogo.gogobetting.global.internal.stage.stub.MatchApiInfo
import org.springframework.stereotype.Component

@Component
class StageApiImpl(
    private val stageClient: StageClient
) : StageApi {
    override fun matchApiInfo(matchLId: Long): MatchApiInfo =
        stageClient.matchApiInfo(matchLId)
}
