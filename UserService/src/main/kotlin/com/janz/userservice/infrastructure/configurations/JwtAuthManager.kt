package com.janz.userservice.infrastructure.configurations

import com.janz.userservice.application.ports.driven.repositories.UserRepository
import com.janz.userservice.infrastructure.configurations.entities.BearerToken
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@Component
class JwtAuthManager (
    private val jwtSupport: JwtSupport,
    private val userRepository: UserRepository
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.justOrEmpty(authentication)
            .filter { auth -> auth is BearerToken}
            .cast(BearerToken::class.java)
            .flatMap { jwt -> mono { validateToken(jwt) } }
            .onErrorMap { error ->
                when (error) {
                    is ResponseStatusException -> error
                    is IllegalArgumentException -> ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        error.message)
                    else -> ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Authentication error: $error")
                }
            }
    }

    private fun validateToken(token: BearerToken): Authentication {
        val username = jwtSupport.getUserName(token)
        val user = userRepository.findUserByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        if (jwtSupport.isValid(token)) {
            val role = user.role.name.uppercase();
            val authorities = mapRoleToAuthorities(role)
            return UsernamePasswordAuthenticationToken(
                user.username,
                null,
                authorities
            )
        }
        throw IllegalArgumentException("Invalid token")
    }

    private fun mapRoleToAuthorities(role: String): List<SimpleGrantedAuthority> {
        return when (role.uppercase()) {
            "ADMIN" -> listOf(
                SimpleGrantedAuthority("ROLE_ADMIN"),
                SimpleGrantedAuthority("ROLE_MODERATOR"),
                SimpleGrantedAuthority("ROLE_CLIENT")
            )
            "CLIENT" -> listOf(SimpleGrantedAuthority("ROLE_CLIENT"))
            else -> emptyList()
        }
    }

}