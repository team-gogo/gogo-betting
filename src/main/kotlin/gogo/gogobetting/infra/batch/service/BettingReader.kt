package gogo.gogobetting.infra.batch.service

import gogo.gogobetting.infra.batch.dto.BettingRow
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration("springBatchBettingReader")
class BettingReader {

    @Bean
    @StepScope
    fun jdbcBettingReader(
        @Value("#{jobParameters['matchId']}") matchId: Long?,
        dataSource: DataSource
    ): JdbcCursorItemReader<BettingRow> {
        val validMatchId = matchId ?: throw IllegalArgumentException("matchId is required")

        return JdbcCursorItemReader<BettingRow>().apply {
            setDataSource(dataSource)
            setSql("""
                SELECT id, match_id, student_id, point, predicted_win_team_id
                FROM tbl_betting
                WHERE match_id = ? AND status = 'CONFIRMED'
                ORDER BY id
            """.trimIndent())
            setPreparedStatementSetter { ps -> ps.setLong(1, validMatchId) }
            setRowMapper { rs, _ ->
                BettingRow(
                    id = rs.getLong("id"),
                    matchId = rs.getLong("match_id"),
                    studentId = rs.getLong("student_id"),
                    point = rs.getLong("point"),
                    predictedWinTeamId = rs.getLong("predicted_win_team_id")
                )
            }
        }
    }
}
