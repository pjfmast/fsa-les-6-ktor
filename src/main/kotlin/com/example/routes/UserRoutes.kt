package com.example.routes

import com.example.dao.daoInMemory
import com.example.dto.UserDto
import com.example.dto.UserRegisterDto
import com.example.dto.UserSignInDto
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRouting() {
    authenticate {
        get("/validate") {
            val principal = call.principal<JWTPrincipal>()
            val email = principal?.get("email")
            val userType = principal?.get("userType")
            call.respondText("Hello, $email ($userType) you have validated access!")
        }
    }

    get("/") {
        call.respondText("Hello to FSA les 6!")
    }

    get("/users") {
        call.respond(daoInMemory.allUsers())
    }

    get("/users/{id}") {
        call.parameters["id"]?.let { stringId ->
            val id: Int? = stringId.toIntOrNull()
            if (id == null) {
                throw WrongIdFormatException()
            } else {
                call.respond<UserDto>(daoInMemory.allUsers().getOrNull(id) ?: throw WrongIdRangeException())
            }
        }
    }

    post("/signin") {
        val user = call.receive<UserSignInDto>()
        val signedIn = daoInMemory.signInUser(user)
        if (signedIn != null) {
            val jwt = createJWT(signedIn.email, signedIn.userType)
            call.respond(message = mapOf("token" to jwt))
        } else {
            throw WrongSigninException()
        }
    }

    post("/signup") {
        val user = call.receive<UserRegisterDto>()
        val userId = daoInMemory.registerUser(user)
        if (userId != null) {
            val jwt = createJWT(user.email, user.userType)
            call.respond(message = mapOf("token" to jwt))
        } else {
            throw AlreadyExistsException(user.email)
        }
    }
}
