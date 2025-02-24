package gogo.gogobetting.global.feign.client

import gogo.gogobetting.global.internal.stage.stub.MatchApiInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "gogo-stage")
interface StageClient {
    @GetMapping("/stage/api/match/info")
    fun matchApiInfo(
        @RequestParam matchId: Long,
    ): MatchApiInfo
}
