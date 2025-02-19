package gogo.gogobetting.global.error

open class BettingException(
    override val message: String,
    val status: Int
) : RuntimeException(message)
