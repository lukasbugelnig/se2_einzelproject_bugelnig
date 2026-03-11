package at.aau.serg.services

import at.aau.serg.models.GameResult
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class GameResultServiceTests {

    private lateinit var service: GameResultService

    @BeforeEach
    fun setup() {
        service = GameResultService()
    }

    @Test
    fun test_getGameResults_emptyList() {
        val result = service.getGameResults()

        assertEquals(emptyList<GameResult>(), result)
    }

    @Test
    fun test_addGameResult_getGameResults_containsSingleElement() {
        val gameResult = GameResult(1, "player1", 17, 15.3)

        service.addGameResult(gameResult)
        val res = service.getGameResults()

        assertEquals(1, res.size)
        assertEquals(gameResult, res[0])
    }

    @Test
    fun test_getGameResultById_existingId_returnsObject() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(1)

        assertEquals(gameResult, res)
    }

    @Test
    fun test_getGameResultById_nonexistentId_returnsNull() {
        val gameResult = GameResult(1, "player1", 17, 15.3)
        service.addGameResult(gameResult)

        val res = service.getGameResult(22)

        assertNull(res)
    }

    @Test
    fun test_addGameResult_multipleEntries_correctId() {
        val gameResult1 = GameResult(0, "player1", 17, 15.3)
        val gameResult2 = GameResult(0, "player2", 25, 16.0)

        service.addGameResult(gameResult1)
        service.addGameResult(gameResult2)

        val res = service.getGameResults()

        assertEquals(2, res.size)

        assertEquals(gameResult1, res[0])
        assertEquals(1, res[0].id)

        assertEquals(gameResult2, res[1])
        assertEquals(2, res[1].id)
    }
    @Test
    fun test_getLeaderboard_sortingLogic() {
        val slow = GameResult(1, "slow", 100, 50.0)
        val fast = GameResult(2, "fast", 100, 10.0) // Gleicher Score, weniger Zeit
        val lowScore = GameResult(3, "low", 50, 5.0)

        service.addGameResult(slow)
        service.addGameResult(fast)
        service.addGameResult(lowScore)

        val leader = service.getLeaderboard()

        assertEquals(3, leader.size)
        assertEquals("fast", leader[0].playerName) // Gewinnt durch Zeit
        assertEquals("slow", leader[1].playerName)
        assertEquals("low", leader[2].playerName)  // Letzter durch Score
    }

    @Test
    fun test_deleteGameResult_removesElement() {
        val gameResult = GameResult(1, "DeleteMe", 10, 10.0)
        service.addGameResult(gameResult)

        // Sicherstellen, dass es da ist
        assertEquals(1, service.getGameResults().size)

        // Löschen
        service.deleteGameResult(1) // oder wie auch immer deine Methode heißt

        // Prüfen, ob es weg ist
        assertEquals(0, service.getGameResults().size)
    }
}