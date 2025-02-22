package gogo.gogobetting.domain.batch.root.presentation

import gogo.gogobetting.domain.batch.root.application.BatchService
import gogo.gogobetting.domain.batch.root.application.dto.BatchDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/betting")
class BatchController(
    private val batchService: BatchService,
) {

    @PostMapping("/batch/{match_id}")
    fun batch(
        @PathVariable("match_id") matchId: Long,
        @RequestBody @Valid dto: BatchDto
    ): ResponseEntity<Void> {
        batchService.batch(matchId, dto)
        return ResponseEntity(HttpStatus.OK)
    }

}
