package at.aau.serg.controllers

import at.aau.serg.models.GameResult
import at.aau.serg.services.GameResultService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(
    private val gameResultService: GameResultService
) {

    //@GetMapping
   // fun getLeaderboard(): List<GameResult> =
     //   gameResultService.getGameResults().sortedWith(compareBy({ -it.score }, { it.id }))

    //neue Methode die direkt gameResultService.getLeaderboard aufruft
   //2. Version 2.2.1
//     @GetMapping
//    fun getLeaderboard(): List<GameResult> {
//        return gameResultService.getLeaderboard()
//    }


    //3. Version bei 2.2
    @GetMapping
    fun getLeaderboard(
        @RequestParam(required = false) rank: Int?
        //requiered false : Programm stürzt nicht ab wenn es nicht mitgeschickt wird
        //rankt: int? : das Fragezeichen macht die Variable nullable - darf Null sein
    ): List<GameResult> {
        val fullLeaderboard = gameResultService.getLeaderboard()

        // 1. fall - kein rang übergeben - alle Einträge werden angezeigt
        if (rank == null) {
            return fullLeaderboard
        }

        // 2. Prüfung: Ist der Rang im gültigen Bereich? (1 bis Listenlänge)
        if (rank < 1 || rank > fullLeaderboard.size) {
            throw org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.BAD_REQUEST,
                "Ungültiger Rang: $rank existiert nicht."
            )
        }
        // ... (hier steht dein bisheriger Code mit der if-Abfrage für rank < 1)

        // 3. Den Bereich berechnen
        // Achtung: Der User gibt Rang 1 ein, aber in der Liste ist das Index 0!
        val centerIndex = rank - 1

        // Wir berechnen den Start (3 davor, aber mindestens 0)
        val startIndex = (centerIndex - 3).coerceAtLeast(0)

        // Wir berechnen das Ende (3 danach, aber maximal das Listenende)
        val endIndex = (centerIndex + 3).coerceAtMost(fullLeaderboard.size - 1)

        // Wir schneiden den Teil aus und geben ihn zurück
        return fullLeaderboard.slice(startIndex..endIndex)

    }



}