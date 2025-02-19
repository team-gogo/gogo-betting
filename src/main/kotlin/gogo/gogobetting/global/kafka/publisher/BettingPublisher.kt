package gogo.gogobetting.global.kafka.publisher

import gogo.gogobetting.global.publisher.TransactionEventPublisher
import org.springframework.stereotype.Component

@Component
class BettingPublisher(
    private val transactionEventPublisher: TransactionEventPublisher
){

}
