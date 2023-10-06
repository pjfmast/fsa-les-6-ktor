package com.example.dao

import com.example.dto.UserDto
import com.example.dto.UserRegisterDto
import com.example.dto.UserSignInDto

object daoInMemory : DAOFacade {
    override suspend fun registerUser(user: UserRegisterDto): Int? {
        val uniqueEmail = users.values.none { it.email == user.email }
        val id = 1 + (users.keys.maxOrNull() ?: 0)
        if (uniqueEmail) {
            users.put(id, User(user.email, user.userType, user.password))
        }
        return if (uniqueEmail) id else null
    }

    override suspend fun allUsers(): List<UserDto> = users.values.map { it.toUserDto() }

    override suspend fun signInUser(user: UserSignInDto): UserDto? {
        val userFound = users.values.firstOrNull { it.email == user.email }
        val validSignIn = userFound != null && userFound.password == user.password

        return if (validSignIn) {
            userFound?.toUserDto()
        } else {
            null
        }
    }
}

private val users = mutableMapOf<Int, User>(
    1 to User("John@gmail.com", "customer"),
    2 to User("Kate@gmail.com", "staff"),
    3 to User("Mike@gmail.com", "customer")
)

private data class User(
    val email: String,
    val userType: String,
    val password: String = "password123"
)

private fun User.toUserDto() = UserDto(this.email, this.userType)
