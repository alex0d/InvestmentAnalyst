package ru.alex0d.investmentanalyst.controller

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

    @GetMapping("/users")
    fun getUsers(): List<User> = adminService.getUsers()

    @PutMapping("/users/{id}")
    fun updateUser(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Int,
        @RequestBody updateUserDto: UpdateUserDto
    ): ResponseEntity<String> {
        if (user.id == id && updateUserDto.role != null) {
            return ResponseEntity.badRequest().body("You can't change your own role")
        }
        adminService.updateUser(id, updateUserDto)
        return ResponseEntity.ok("User updated")
    }

    @DeleteMapping("/users/{id}")
    fun deleteUser(
        @AuthenticationPrincipal user: User,
        @PathVariable id: Int
    ): ResponseEntity<String> {
        if (user.id == id) {
            return ResponseEntity.badRequest().body("You can't delete yourself")
        }
        adminService.deleteUser(id)
        return ResponseEntity.ok("User deleted")
    }
}