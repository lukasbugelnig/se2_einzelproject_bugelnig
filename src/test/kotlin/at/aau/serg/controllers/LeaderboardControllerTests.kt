package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class LeaderboardControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: LeaderboardController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = LeaderboardController(mockedService)
    }

//    @Test
//    fun test_getLeaderboard_correctScoreSorting() {
//        val first = GameResult(1, "first", 20, 20.0)
//        val second = GameResult(2, "second", 15, 10.0)
//        val third = GameResult(3, "third", 10, 15.0)
//
//        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))
//
//        val res: List<GameResult> = controller.getLeaderboard()
//
//        verify(mockedService).getGameResults()
//        assertEquals(3, res.size)
//        assertEquals(first, res[0])
//        assertEquals(second, res[1])
//        assertEquals(third, res[2])
//    }
@Test
fun test_getLeaderboard_correctScoreSorting() {
    val first = GameResult(1, "first", 20, 20.0)
    val second = GameResult(2, "second", 15, 10.0)
    val third = GameResult(3, "third", 10, 15.0)

    // ÄNDERUNG: Wir mocken jetzt die neue Methode 'getLeaderboard'
    whenever(mockedService.getLeaderboard()).thenReturn(listOf(first, second, third))

    // ÄNDERUNG: Wir übergeben 'null' für den (jetzt optionalen) Rank-Parameter
    val res: List<GameResult> = controller.getLeaderboard(null)

    // ÄNDERUNG: Wir verifizieren den Aufruf der neuen Methode
    verify(mockedService).getLeaderboard()

    assertEquals(3, res.size)
    assertEquals(first, res[0])
    assertEquals(second, res[1])
    assertEquals(third, res[2])
}
/*
    @Test
    fun test_getLeaderboard_sameScore_CorrectIdSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 20, 10.0)
        val third = GameResult(3, "third", 20, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val res: List<GameResult> = controller.getLeaderboard()

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }
*/
    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        // Drei Spieler mit gleichem Score (20), aber unterschiedlicher Zeit
        val slow = GameResult(id = 1, playerName = "slow", score = 20, timeInSeconds = 30.0)
        val fast = GameResult(id = 2, playerName = "fast", score = 20, timeInSeconds = 10.0)
        val medium = GameResult(id = 3, playerName = "medium", score = 20, timeInSeconds = 20.0)

        // Wir simulieren, dass der Service diese Liste (unsortiert) zurückgibt
        // Da wir im Controller jetzt .getLeaderboard() aufrufen sollten:
        whenever(mockedService.getLeaderboard()).thenReturn(listOf(fast, medium, slow))

        val res: List<GameResult> = controller.getLeaderboard(null)

        assertEquals(3, res.size)
        assertEquals(fast, res[0])   // 10.0s -> Platz 1
        assertEquals(medium, res[1]) // 20.0s -> Platz 2
        assertEquals(slow, res[2])   // 30.0s -> Platz 3
    }
//2.2.3

    @Test
    fun test_getLeaderboard_withValidRank_returnsSlice() {
        // Wir erstellen 10 Dummy-Ergebnisse
        val results = (1..10).map { GameResult(it.toLong(), "Player$it", 100 - it, 10.0) }
        whenever(mockedService.getLeaderboard()).thenReturn(results)

        // Wir fragen nach Rang 5 (Index 4)
        // Erwartet: Plätze 2, 3, 4, 5, 6, 7, 8 (da +/- 3)
        val res = controller.getLeaderboard(5)

        assertEquals(7, res.size)
        assertEquals("Player2", res.first().playerName)
        assertEquals("Player8", res.last().playerName)
    }

    @Test
    fun test_getLeaderboard_withInvalidRank_throwsBadRequest() {
        val results = listOf(GameResult(1, "Player", 100, 10.0))
        whenever(mockedService.getLeaderboard()).thenReturn(results)

        // Wir fragen nach einem Rang, den es nicht gibt (z.B. 10)
        val exception = org.junit.jupiter.api.assertThrows<org.springframework.web.server.ResponseStatusException> {
            controller.getLeaderboard(10)
        }

        assertEquals(org.springframework.http.HttpStatus.BAD_REQUEST, exception.statusCode)
    }
}