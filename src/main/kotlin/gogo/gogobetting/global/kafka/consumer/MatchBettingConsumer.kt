package gogo.gogobetting.global.kafka.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.listener.AcknowledgingMessageListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Service

@Service
class MatchBettingConsumer(
    private val objectMapper: ObjectMapper,
) : AcknowledgingMessageListener<String, String> {

    private val log = LoggerFactory.getLogger(this::class.java.simpleName)

    override fun onMessage(data: ConsumerRecord<String, String>, acknowledgment: Acknowledgment?) {
        TODO("Not yet implemented")
    }


}
