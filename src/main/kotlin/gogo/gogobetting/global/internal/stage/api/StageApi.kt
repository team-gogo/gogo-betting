package gogo.gogobetting.global.internal.stage.api

import gogo.gogobetting.global.internal.stage.stub.MatchApiInfo

interface StageApi {
    fun matchApiInfo(matchLId: Long): MatchApiInfo
}
