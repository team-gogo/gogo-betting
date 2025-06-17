package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.infra.batch.dto.BettingResultJdbcDto
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JdbcBatchItemWriter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class BettingWriterConfig {

    @Bean
    @StepScope
    fun bettingResultJdbcWriter(
        dataSource: DataSource
    ): JdbcBatchItemWriter<BettingResultJdbcDto> {
        return JdbcBatchItemWriter<BettingResultJdbcDto>().apply {
            setDataSource(dataSource)
            setSql("""
                INSERT INTO tbl_betting_result (betting_id, earned_point, is_cancelled, is_predicted)
                VALUES (?, ?, ?, ?)
            """.trimIndent())
            setItemPreparedStatementSetter { item, ps ->
                ps.setLong(1, item.bettingId)
                ps.setLong(2, item.earnedPoint)
                ps.setBoolean(3, false)
                ps.setBoolean(4, item.isPredicted)
            }
            afterPropertiesSet()
        }
    }

}
