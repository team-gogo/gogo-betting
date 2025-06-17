package gogo.gogobetting.domain.betting.result.persistence

import org.jetbrains.exposed.sql.Table

object BettingResultTable : Table("tbl_betting_result") {
    val bettingId = long("betting_id")
    val earnedPoint = long("earned_point")
    val isCancelled = bool("is_cancelled")
    val isPredicted = bool("is_predicted")
}
