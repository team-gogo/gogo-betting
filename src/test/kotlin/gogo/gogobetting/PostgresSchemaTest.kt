package gogo.gogobetting

import gogo.gogobetting.domain.batch.detail.persistence.BatchDetail
import gogo.gogobetting.domain.batch.detail.persistence.BatchDetailRepository
import gogo.gogobetting.domain.batch.root.persistence.Batch
import gogo.gogobetting.domain.batch.root.persistence.BatchRepository
import gogo.gogobetting.domain.betting.root.persistence.Betting
import gogo.gogobetting.domain.betting.root.persistence.BettingRepository
import gogo.gogobetting.global.config.QueryDslConfig
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(QueryDslConfig::class)
@EnabledIfEnvironmentVariable(named = "DATABASE_URL", matches = "jdbc:postgresql:.*")
class PostgresSchemaTest @Autowired constructor(
    private val bets: BettingRepository,
    private val batches: BatchRepository,
    private val batchDetails: BatchDetailRepository,
    private val jdbc: JdbcTemplate,
) {
    @Test
    fun `flyway schema validates betting and rejects unknown status`() {
        val bet = bets.saveAndFlush(Betting.of(matchId = 901L, studentId = 902L, point = 30L, predictedWinTeamId = 903L))

        assertTrue(bet.id > 0)
        assertTrue(bets.findAllByMatchId(901L).any { it.id == bet.id })
        assertEquals("CONFIRMED", jdbc.queryForObject("SELECT status FROM tbl_betting WHERE id = ?", String::class.java, bet.id))

        assertFailsWith<DataIntegrityViolationException> {
            jdbc.update(
                """INSERT INTO tbl_betting(match_id, student_id, point, predicted_win_team_id, status, created_at)
                   VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)""",
                901L,
                902L,
                30L,
                903L,
                "INVALID",
            )
        }
    }

    @Test
    fun `batch detail is unique per batch and metadata tables are present`() {
        val batch = batches.saveAndFlush(Batch.of(matchId = 904L, studentId = 905L, startTime = LocalDateTime.now(), endTime = null))
        val detail = batchDetails.saveAndFlush(BatchDetail.of(batchId = batch.id, victoryTeamId = 906L, aTeamScore = 2, bTeamScore = 1))

        assertEquals(batch.id, batchDetails.findById(detail.id).orElseThrow().batchId)
        jdbc.queryForObject("SELECT COUNT(*) FROM batch_job_instance", Long::class.java)

        assertFailsWith<DataIntegrityViolationException> {
            jdbc.update(
                "INSERT INTO tbl_batch_detail(batch_id, victory_team_id, a_team_score, b_team_score) VALUES (?, ?, ?, ?)",
                batch.id,
                906L,
                3,
                1,
            )
        }
    }
}
