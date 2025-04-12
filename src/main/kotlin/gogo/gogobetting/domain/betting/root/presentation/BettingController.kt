package gogo.gogobetting.domain.betting.root.presentation

import gogo.gogobetting.domain.betting.root.application.BettingService
import gogo.gogobetting.domain.betting.root.application.dto.BettingBundleDto
import gogo.gogobetting.domain.betting.root.application.dto.BettingDto
import gogo.gogobetting.domain.betting.root.application.dto.TotalBettingPointDto
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/betting")
class BettingController(
    private val bettingService: BettingService,
) {

    @PostMapping("/{match_id}")
    fun bet(
        @PathVariable("match_id") matchId: Long,
        @RequestBody @Valid dto: BettingDto,
    ): ResponseEntity<Unit> {
        bettingService.bet(matchId, dto)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/bundle")
    fun query(
        @RequestParam @Valid @NotNull matchIds: List<Long>,
        @RequestParam @Valid @NotNull studentId: Long
    ): ResponseEntity<BettingBundleDto> {
        val response = bettingService.bundle(matchIds, studentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/point")
    fun totalBettingPoint(
        @RequestParam @Valid @NotNull matchIds: List<Long>,
        @RequestParam @Valid @NotNull studentId: Long
    ): ResponseEntity<TotalBettingPointDto> {
        val response = bettingService.totalBettingPoint(matchIds, studentId)
        return ResponseEntity.ok(response)
    }

}
