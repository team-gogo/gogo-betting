package gogo.gogobetting.global.kafka.consumer.dto

data class BatchCancelDeleteTempPointFailedEvent(
    val id: String,
    val batchId: Long
)
