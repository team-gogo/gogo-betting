package gogo.gogobetting.global.kafka.publisher

import gogo.gogobetting.domain.batch.root.event.BatchCancelEvent
import gogo.gogobetting.domain.batch.root.event.MatchBatchEvent
import gogo.gogobetting.global.kafka.properties.KafkaTopics.BATCH_CANCEL
import gogo.gogobetting.global.kafka.properties.KafkaTopics.MATCH_BATCH
import gogo.gogobetting.global.publisher.TransactionEventPublisher
import org.springframework.stereotype.Component
import java.util.*

@Component
class BatchPublisher(
    private val transactionEventPublisher: TransactionEventPublisher
){

    fun publishBettingBatchEvent(
        event: MatchBatchEvent,
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = MATCH_BATCH,
            key = key,
            event = event
        )
    }

    fun publishBatchCancelEvent(
        event: BatchCancelEvent,
    ) {
        val key = UUID.randomUUID().toString()
        transactionEventPublisher.publishEvent(
            topic = BATCH_CANCEL,
            key = key,
            event = event
        )
    }

}
