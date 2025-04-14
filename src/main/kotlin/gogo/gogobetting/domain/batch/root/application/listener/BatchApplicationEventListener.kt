package gogo.gogobetting.domain.batch.root.application.listener

import gogo.gogobetting.domain.batch.root.event.BatchCancelEvent
import gogo.gogobetting.global.kafka.publisher.BatchPublisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class BatchApplicationEventListener(
    private val batchPublisher: BatchPublisher,
) {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun batchCancelEvent(event: BatchCancelEvent) {
        with(event) {
            log.info("published batch cancel application event: {}", id)
            batchPublisher.publishBatchCancelEvent(event)
        }
    }

}
