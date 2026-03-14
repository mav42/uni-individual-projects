package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    @GetMapping
    fun getLeaderboard(rank: Int = 0): ResponseEntity<*> {
        val sortedResults = gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.timeInSeconds }))
        if (rank == 0)
            return ResponseEntity.ok(sortedResults)
        else if (rank > 0 && rank <= sortedResults.size) {
            // GameResult is 0-indexed, so rank - 1
            val targetIndex = rank - 1
            val range = 3

            // Calculate leaderboard range: target player + 3 above + 3 below
            val startIndex = maxOf(0, targetIndex - range)  // startindex is inclusive
            val endIndex = minOf(sortedResults.size, targetIndex + range + 1) // +1 because endIndex is exclusive

            return ResponseEntity.ok(sortedResults.subList(startIndex, endIndex))
        }
        else
            return ResponseEntity("Rank is invalid.", HttpStatus.BAD_REQUEST)
    }
}