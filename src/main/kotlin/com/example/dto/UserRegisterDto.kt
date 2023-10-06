package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterDto (
    val email: String,
    val userType: String,
    val password: String
)