package ru.alex0d.investmentanalyst.service

import org.springframework.stereotype.Service
import ru.alex0d.investmentanalyst.dto.UpdateUserDto
import ru.alex0d.investmentanalyst.model.Role
import ru.alex0d.investmentanalyst.model.User
import ru.alex0d.investmentanalyst.repository.UserRepository

@Service
class AdminService(
    private val userRepository: UserRepository,
) {
    fun getUsers(): List<User> = userRepository.findAll()

    fun updateUser(id: Int, updateUserDto: UpdateUserDto) {
        val user = userRepository.findById(id).get()
        updateUserDto.firstname?.let { user.firstname = it }
        updateUserDto.lastname?.let { user.lastname = it }
        updateUserDto.email?.let { user.email = it }
        updateUserDto.role?.let { user.role = Role.valueOf(it) }
        userRepository.save(user)
    }

    fun deleteUser(id: Int) = userRepository.deleteById(id)
}