package com.example.dao

import com.example.dto.UserSignInDto
import com.example.dto.UserDto
import com.example.dto.UserRegisterDto

interface DAOFacade {
    suspend fun registerUser(user: UserRegisterDto): Int?
    suspend fun allUsers(): List<UserDto>
    suspend fun signInUser(user: UserSignInDto): UserDto?
}