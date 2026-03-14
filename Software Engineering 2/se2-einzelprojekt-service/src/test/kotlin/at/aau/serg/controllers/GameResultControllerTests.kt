package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import org.mockito.Mockito.`when` as whenever // when is a reserved keyword in Kotlin

class GameResultControllerTests {

    private lateinit var mockedService: GameResultService
    private lateinit var controller: GameResultController

    @BeforeEach
    fun setup() {
        mockedService = mock<GameResultService>()
        controller = GameResultController(mockedService)
    }

    @Test
    fun test_getGameResult_existingId_returnsGameResult() {
        val gameResult = GameResult(1, "player1", 100, 50.0)
        whenever(mockedService.getGameResult(1)).thenReturn(gameResult)

        val result = controller.getGameResult(1)

        verify(mockedService).getGameResult(1)
        assertEquals(gameResult, result)
    }

    @Test
    fun test_getGameResult_nonexistentId_returnsNull() {
        whenever(mockedService.getGameResult(999)).thenReturn(null)

        val result = controller.getGameResult(999)

        verify(mockedService).getGameResult(999)
        assertNull(result)
    }

    @Test
    fun test_getAllGameResults_returnsList() {
        val gameResults = listOf(
            GameResult(1, "player1", 100, 50.0),
            GameResult(2, "player2", 90, 45.0)
        )
        whenever(mockedService.getGameResults()).thenReturn(gameResults)

        val result = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(2, result.size)
        assertEquals(gameResults, result)
    }

    @Test
    fun test_getAllGameResults_emptyList() {
        whenever(mockedService.getGameResults()).thenReturn(emptyList())

        val result = controller.getAllGameResults()

        verify(mockedService).getGameResults()
        assertEquals(0, result.size)
    }

    @Test
    fun test_addGameResult_callsService() {
        val gameResult = GameResult(0, "player1", 100, 50.0)

        controller.addGameResult(gameResult)

        verify(mockedService).addGameResult(gameResult)
    }

    @Test
    fun test_deleteGameResult_callsService() {
        whenever(mockedService.deleteGameResult(1)).thenReturn(true)

        controller.deleteGameResult(1)

        verify(mockedService).deleteGameResult(1)
    }

    @Test
    fun test_deleteGameResult_nonexistentId_callsService() {
        whenever(mockedService.deleteGameResult(999)).thenReturn(false)

        controller.deleteGameResult(999)

        verify(mockedService).deleteGameResult(999)
    }
}