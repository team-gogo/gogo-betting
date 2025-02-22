package gogo.gogobetting.global.kafka.publisher

import gogo.gogobetting.domain.betting.root.event.MatchBettingEvent
import gogo.gogobetting.global.kafka.properties.KafkaTopics.MATCH_BETTING
import gogo.gogobetting.global.publisher.TransactionEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class BettingPublisher(
    private val transactionEventPublisher: TransactionEventPublisher
){

    fun publishMatchBettingEvent(
        event: MatchBettingEvent
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = MATCH_BETTING,
            key = key,
            event = event
        )
    }

}
