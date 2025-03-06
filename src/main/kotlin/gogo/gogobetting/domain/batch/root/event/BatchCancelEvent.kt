package gogo.gogobetting.domain.batch.root.event

data class BatchCancelEvent(
    val id: String,
    val batchId: Long,
    val matchId: Long,
)
