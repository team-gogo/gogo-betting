package gogo.gogobetting.domain.health

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/betting")
class HealthCheckController {

    @GetMapping("/health")
    fun healthCheck() = "GOGO Betting Service OK"

}
