package gogo.gogobetting.global.internal.stage.api

import gogo.gogobetting.global.internal.stage.stub.IsMaintainerDto

interface StageApi {
    fun isMaintainer(matchLId: Long, studentId: Long): IsMaintainerDto
}
