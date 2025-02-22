package gogo.gogobetting.global.feign.client

import gogo.gogobetting.global.internal.stage.stub.IsMaintainerDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "gogo-stage")
interface StageClient {
    @GetMapping("/stage/maintainer")
    fun isMaintainer(
        @RequestParam matchId: Long,
        @RequestParam studentId: Long
    ): IsMaintainerDto
}
