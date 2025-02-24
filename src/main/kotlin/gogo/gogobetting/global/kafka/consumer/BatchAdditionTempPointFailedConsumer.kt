package gogo.gogobetting.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogobetting.global.kafka.consumer.dto.BatchAdditionTempPointFailedEvent
import gogo.gogobetting.global.kafka.properties.KafkaTopics.BATCH_ADDITION_TEMP_POINT_FAILED
import gogo.gogobetting.global.saga.AdditionTempPointFailedSaga
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class BatchAdditionTempPointFailedConsumer(
    private val objectMapper: ObjectMapper,
    private val additionTempPointFailedSaga: AdditionTempPointFailedSaga
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [BATCH_ADDITION_TEMP_POINT_FAILED],
        groupId = "gogo",
        containerFactory = "batchAdditionTempPointFailedEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), BatchAdditionTempPointFailedEvent::class.java)
        log.info("${BATCH_ADDITION_TEMP_POINT_FAILED}_topic, key: $key, event: $event")

        additionTempPointFailedSaga.cancelledBatch(event.batchId)

        acknowledgment!!.acknowledge()
    }


}
