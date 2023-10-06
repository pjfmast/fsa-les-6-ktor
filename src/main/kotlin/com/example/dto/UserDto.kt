package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    val userType: String
)

