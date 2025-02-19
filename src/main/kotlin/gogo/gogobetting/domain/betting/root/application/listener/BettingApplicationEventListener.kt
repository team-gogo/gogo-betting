package gogo.gogobetting.domain.betting.root.application.listener

import gogo.gogobetting.domain.betting.root.event.MatchBettingEvent
import gogo.gogobetting.global.kafka.publisher.BettingPublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BettingApplicationEventListener(
    private val bettingPublisher: BettingPublisher,
) {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun matchBettingEvent(event: MatchBettingEvent) {
        with(event) {
            log.info("published match betting application event: {}", id)
            bettingPublisher.publishMatchBettingEvent(event)
        }
    }

}
