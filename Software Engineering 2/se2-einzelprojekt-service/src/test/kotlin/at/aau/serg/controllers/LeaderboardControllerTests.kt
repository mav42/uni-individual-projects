package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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

    @Test
    fun test_getLeaderboard_correctScoreSorting() {
        val first = GameResult(1, "first", 20, 20.0)
        val second = GameResult(2, "second", 15, 10.0)
        val third = GameResult(3, "third", 10, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val response: ResponseEntity<*> = controller.getLeaderboard()
        val res = response.body as List<GameResult>

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_sameScore_CorrectTimeSorting() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val response: ResponseEntity<*> = controller.getLeaderboard()
        val res = response.body as List<GameResult>

        verify(mockedService).getGameResults()
        assertEquals(3, res.size)
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
    }

    @Test
    fun test_getLeaderboard_CorrectRangeReturn() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)
        val fourth = GameResult(4, "fourth", 15, 10.0)
        val fifth = GameResult(5, "fifth", 15, 15.0)
        val sixth = GameResult(6, "sixth", 15, 20.0)
        val seventh = GameResult(7, "seventh", 10, 10.0)
        val eighth = GameResult(8, "eighth", 10, 15.0)
        val ninth = GameResult(9, "ninth", 10, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third, fourth, fifth, sixth, seventh, eighth, ninth))

        val response: ResponseEntity<*> = controller.getLeaderboard(5)
        val res = response.body as List<GameResult>

        verify(mockedService).getGameResults()
        assertEquals(7, res.size)
        // ranks before targeted rank
        assertEquals(second, res[0])
        assertEquals(third, res[1])
        assertEquals(fourth, res[2])
        // targeted rank
        assertEquals(fifth, res[3])
        // ranks after targeted rank
        assertEquals(sixth, res[4])
        assertEquals(seventh, res[5])
        assertEquals(eighth, res[6])
    }

    @Test
    fun test_getLeaderboard_CorrectRangeReturn_targetTop() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)
        val fourth = GameResult(4, "fourth", 15, 10.0)
        val fifth = GameResult(5, "fifth", 15, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third, fourth, fifth))

        val response: ResponseEntity<*> = controller.getLeaderboard(1)
        val res = response.body as List<GameResult>

        verify(mockedService).getGameResults()
        assertEquals(4, res.size)
        // targeted rank
        assertEquals(first, res[0])
        // ranks after targeted rank
        assertEquals(second, res[1])
        assertEquals(third, res[2])
        assertEquals(fourth, res[3])
    }

    @Test
    fun test_getLeaderboard_CorrectRangeReturn_targetTowardsBottom() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)
        val fourth = GameResult(4, "fourth", 15, 10.0)
        val fifth = GameResult(5, "fifth", 15, 15.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third, fourth, fifth))

        val response: ResponseEntity<*> = controller.getLeaderboard(4)
        val res = response.body as List<GameResult>

        verify(mockedService).getGameResults()
        assertEquals(5, res.size)
        // ranks before targeted rank
        assertEquals(first, res[0])
        assertEquals(second, res[1])
        assertEquals(third, res[2])
        // targeted rank
        assertEquals(fourth, res[3])
        //rank after targeted rank
        assertEquals(fifth, res[4])
    }

    @Test
    fun test_getLeaderboard_BadRequest() {
        val first = GameResult(1, "first", 20, 10.0)
        val second = GameResult(2, "second", 20, 15.0)
        val third = GameResult(3, "third", 20, 20.0)

        whenever(mockedService.getGameResults()).thenReturn(listOf(second, first, third))

        val response: ResponseEntity<*> = controller.getLeaderboard(-1)
        val resBody = response.body as String
        val resStatus = response.statusCode

        verify(mockedService).getGameResults()
        assertEquals("Rank is invalid.", resBody)
        assertEquals(HttpStatus.BAD_REQUEST, resStatus)
    }
}