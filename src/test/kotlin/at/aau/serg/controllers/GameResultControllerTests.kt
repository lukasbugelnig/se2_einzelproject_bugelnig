package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        // Wir erstellen eine Attrappe des Services
        mockedService = mock(GameResultService::class.java)
        // Wir initialisieren den Controller mit diesem Mock
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_byId() {
        // Vorbereitung: Ein einzelnes Ergebnis simulieren
        val expected = GameResult(1, "Lukas", 100, 10.0)
        `when`(mockedService.getGameResult(1L)).thenReturn(expected)

        // Ausführung: Controller nach ID 1 fragen
        val actual = controller.getGameResult(1L)

        // Überprüfung: Hat der Service die richtige ID erhalten und das Ergebnis geliefert?
        assertEquals(expected, actual)
        verify(mockedService).getGameResult(1L)
    }

    @Test
    fun test_getAllGameResults() {
        // Vorbereitung: Eine Liste simulieren
        val list = listOf(GameResult(1, "Player", 100, 10.0))
        `when`(mockedService.getGameResults()).thenReturn(list)

        // Ausführung: Die Methode aufrufen, die du im Code hast
        val res = controller.getAllGameResults()

        // Überprüfung
        assertEquals(list, res)
        verify(mockedService).getGameResults()
    }

    @Test
    fun test_addGameResult() {
        val newResult = GameResult(0, "NewPlayer", 50, 20.0)

        // Ausführung
        controller.addGameResult(newResult)

        // Überprüfung: Wurde addGameResult im Service aufgerufen?
        verify(mockedService).addGameResult(newResult)
    }

    @Test
    fun test_deleteGameResult() {
        // Ausführung
        controller.deleteGameResult(1L)

        // Überprüfung: Wurde die Löschmethode mit der ID 1 aufgerufen?
        verify(mockedService).deleteGameResult(1L)
    }
}