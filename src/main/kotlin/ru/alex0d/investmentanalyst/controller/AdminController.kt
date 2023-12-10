package ru.alex0d.investmentanalyst.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import ru.alex0d.investmentanalyst.dto.UpdateUserDto
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.service.AdminService

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val adminService: AdminService
) {

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/users")
    fun getUsers(): List<User> = adminService.getUsers()

    @Operation(summary = "Update user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User updated successfully"),
            ApiResponse(responseCode = "400", description = "Bad request")
        ]
    )
    @PutMapping("/users/{id}")
    fun updateUser(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "User id") @PathVariable id: Int,
        @RequestBody updateUserDto: UpdateUserDto
    ): ResponseEntity<String> {
        if (user.id == id && updateUserDto.role != null) {
            return ResponseEntity.badRequest().body("You can't change your own role")
        }
        adminService.updateUser(id, updateUserDto)
        return ResponseEntity.ok("User updated")
    }

    @Operation(summary = "Delete user")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User deleted successfully"),
            ApiResponse(responseCode = "400", description = "Bad request. You can't delete yourself")
        ]
    )
    @DeleteMapping("/users/{id}")
    fun deleteUser(
        @AuthenticationPrincipal user: User,
        @Parameter(description = "User id") @PathVariable id: Int
    ): ResponseEntity<String> {
        if (user.id == id) {
            return ResponseEntity.badRequest().body("You can't delete yourself")
        }
        adminService.deleteUser(id)
        return ResponseEntity.ok("User deleted")
    }
}