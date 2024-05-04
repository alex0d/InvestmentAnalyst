package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.*
import ru.alex0d.investmentanalyst.service.WatchlistService

@RestController
@CrossOrigin
@RequestMapping("/api/watchlist")
class WatchlistController(
    private val watchlistService: WatchlistService
) {

    @Operation(summary = "Get all watchlists", description = "Get all watchlists with brief info")
    @ApiResponse(responseCode = "200", description = "Watchlists retrieved successfully")
    @GetMapping
    fun getAllWatchlists(): ResponseEntity<List<WatchlistBriefDto>> {
        val watchlist = watchlistService.getWatchlists()
            ?: return ResponseEntity.notFound().build()

        val watchlistBrief = watchlist.map { WatchlistBriefDto(it.id, it.title, it.items.size) }
        return ResponseEntity.ok(watchlistBrief)
    }

    @Operation(summary = "Get watchlist", description = "Get watchlist with all items")
    @ApiResponse(responseCode = "200", description = "Watchlist retrieved successfully")
    @GetMapping("/{watchlistId}")
    fun getWatchlist(
        @Parameter(description = "Watchlist id") @PathVariable watchlistId: Int,
        @RequestParam(name = "inRange", required = false) inRange: Boolean?
    ):
            ResponseEntity<WatchlistDto> {
        val watchlist = watchlistService.getWatchlist(watchlistId, inRange != null && inRange)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(watchlist)
    }

    @Operation(summary = "Create watchlist", description = "Create watchlist with given title")
    @ApiResponse(responseCode = "200", description = "Watchlist created successfully")
    @PostMapping
    fun createWatchlist(@RequestBody request: CreateWatchlistRequest): ResponseEntity<WatchlistBriefDto> {
        val watchlist = watchlistService.createWatchlist(request.title)
        val watchlistBrief = WatchlistBriefDto(watchlist.id, watchlist.title, watchlist.items.size)
        return ResponseEntity.ok(watchlistBrief)
    }

    @Operation(summary = "Delete watchlist", description = "Delete watchlist with given id")
    @ApiResponse(responseCode = "200", description = "Watchlist deleted successfully")
    @DeleteMapping("/{watchlistId}")
    fun deleteWatchlist(@Parameter(description = "Watchlist id") @PathVariable watchlistId: Int): ResponseEntity<Unit> {
        watchlistService.deleteWatchlist(watchlistId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "Add ticker to watchlist", description = "Add ticker to watchlist with given id")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ticker added successfully"),
            ApiResponse(responseCode = "400", description = "Bad request. Invalid ticker or ticker already in watchlist")
        ]
    )
    @PostMapping("/{watchlistId}")
    fun addTickerToWatchlist(
        @Parameter(description = "Watchlist id") @PathVariable watchlistId: Int,
        @RequestBody request: CreateWatchlistItemRequest
    ):
            ResponseEntity<WatchlistItemDto> {
        val watchlistItemDto = watchlistService.addTickerToWatchlist(watchlistId, request)
            ?: return ResponseEntity.badRequest().build()
        return ResponseEntity.ok(watchlistItemDto)
    }

    @Operation(summary = "Remove ticker from watchlist", description = "Remove ticker from watchlist with given id")
    @ApiResponse(responseCode = "200", description = "Ticker removed successfully")
    @DeleteMapping("/{watchlistId}/{watchlistItemId}")
    fun removeTickerFromWatchlist(
        @Parameter(description = "Watchlist id") @PathVariable watchlistId: Int,
        @Parameter(description = "Watchlist item id") @PathVariable watchlistItemId: Int
    ):
            ResponseEntity<Unit> {
        watchlistService.removeTickerFromWatchlist(watchlistId, watchlistItemId)
        return ResponseEntity.ok().build()
    }

}