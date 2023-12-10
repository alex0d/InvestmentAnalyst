package ru.alex0d.investmentanalyst.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.*
import ru.alex0d.investmentanalyst.service.WatchlistService

@RestController
@RequestMapping("/api/watchlist")
class WatchlistController(
    private val watchlistService: WatchlistService
) {
    @GetMapping
    fun getAllWatchlists(): ResponseEntity<List<WatchlistBriefDto>> {
        val watchlist = watchlistService.getWatchlists()
            ?: return ResponseEntity.notFound().build()

        val watchlistBrief = watchlist.map { WatchlistBriefDto(it.id, it.title, it.items.size) }
        return ResponseEntity.ok(watchlistBrief)
    }

    @GetMapping("/{watchlistId}")
    fun getWatchlist(
        @PathVariable watchlistId: Int,
        @RequestParam(name = "inRange", required = false) inRange: Boolean?
    ):
            ResponseEntity<WatchlistDto> {
        val watchlist = watchlistService.getWatchlist(watchlistId, inRange != null && inRange)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(watchlist)
    }

    @PostMapping
    fun createWatchlist(@RequestBody request: CreateWatchlistRequest): ResponseEntity<WatchlistBriefDto> {
        val watchlist = watchlistService.createWatchlist(request.title)
        val watchlistBrief = WatchlistBriefDto(watchlist.id, watchlist.title, watchlist.items.size)
        return ResponseEntity.ok(watchlistBrief)
    }

    @DeleteMapping("/{watchlistId}")
    fun deleteWatchlist(@PathVariable watchlistId: Int): ResponseEntity<Unit> {
        watchlistService.deleteWatchlist(watchlistId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{watchlistId}")
    fun addTickerToWatchlist(
        @PathVariable watchlistId: Int,
        @RequestBody request: CreateWatchlistItemRequest
    ):
            ResponseEntity<WatchlistItemDto> {
        val watchlistItemDto = watchlistService.addTickerToWatchlist(watchlistId, request)
            ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(watchlistItemDto)
    }

    @DeleteMapping("/{watchlistId}/{watchlistItemId}")
    fun removeTickerFromWatchlist(
        @PathVariable watchlistId: Int,
        @PathVariable watchlistItemId: Int
    ):
            ResponseEntity<Unit> {
        watchlistService.removeTickerFromWatchlist(watchlistId, watchlistItemId)
        return ResponseEntity.ok().build()
    }


}