package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.routes.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<AlreadyExistsException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                hashMapOf("status" to "User already exists"),
            )
        }
        // catch all (custom) exceptions:
        exception<Throwable> { call, cause ->
            call.respondText(text = "$cause", status = HttpStatusCode.BadRequest)
        }
    }

    routing {
        userRouting()
    }

}

class WrongIdFormatException : Exception("id parameter is not an integer:")
class WrongIdRangeException : Exception("id is an integer but outside the range")
class AlreadyExistsException(email: String) : Throwable("user with $email already exists")
class WrongSigninException : Throwable("wrong signing in credentials")
