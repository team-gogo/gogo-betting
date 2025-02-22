package gogo.gogobetting.global.kafka.publisher

import gogo.gogobetting.domain.batch.root.event.BettingBatchEvent
import gogo.gogobetting.global.kafka.properties.KafkaTopics.BETTING_BATCH
import gogo.gogobetting.global.publisher.TransactionEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class BatchPublisher(
    private val transactionEventPublisher: TransactionEventPublisher
){

    fun publishBettingBatchEvent(
        event: BettingBatchEvent,
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = BETTING_BATCH,
            key = key,
            event = event
        )
    }

}
