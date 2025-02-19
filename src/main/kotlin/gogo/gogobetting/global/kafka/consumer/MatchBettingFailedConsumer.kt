package gogo.gogobetting.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import gogo.gogobetting.global.kafka.consumer.dto.MatchBettingFailedEvent
import gogo.gogobetting.global.kafka.properties.KafkaTopics.MATCH_BETTING_FAILED
import gogo.gogobetting.global.saga.MatchBettingFailedSaga
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class MatchBettingFailedConsumer(
    private val objectMapper: ObjectMapper,
    private val matchBettingFailedSaga: MatchBettingFailedSaga
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    @KafkaListener(
        topics = [MATCH_BETTING_FAILED],
        groupId = "gogo",
        containerFactory = "matchBettingFailedEventListenerContainerFactory"
    )
    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        val (key, event) = data.key() to objectMapper.readValue(data.value(), MatchBettingFailedEvent::class.java)
        log.info("${MATCH_BETTING_FAILED}_topic, key: $key, event: $event")

        matchBettingFailedSaga.cancelledBatting(event.bettingId)

        acknowledgment!!.acknowledge()
    }


}
