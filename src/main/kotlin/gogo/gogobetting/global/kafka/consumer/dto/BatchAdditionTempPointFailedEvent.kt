package gogo.gogobetting.global.kafka.consumer.dto

data class BatchAdditionTempPointFailedEvent(
    val id: String,
    val batchId: Long,
)
