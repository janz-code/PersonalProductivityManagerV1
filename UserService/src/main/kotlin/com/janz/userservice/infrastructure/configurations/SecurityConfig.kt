package com.janz.userservice.infrastructure.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@EnableWebFluxSecurity
@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder();

    @Bean
    fun userDetailsService(encoder: PasswordEncoder): MapReactiveUserDetailsService {
        val user = User.builder()
            .username("janz")
            .password(encoder.encode("password"))
            .roles("ADMIN")
            .build()

        return MapReactiveUserDetailsService(user);
    }

    @Bean
    fun sprintSecurityFilterChain(
        converter: JwtServerAuthConverter,
        http: ServerHttpSecurity,
        authManager: JwtAuthManager
    ): SecurityWebFilterChain {

        val filter = AuthenticationWebFilter(authManager);
        filter.setServerAuthenticationConverter(converter)

        http
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .exceptionHandling { spec ->
                spec.authenticationEntryPoint { exchange, _ ->
                    exchange.response.statusCode = HttpStatus.UNAUTHORIZED
                    exchange.response.headers.set(HttpHeaders.WWW_AUTHENTICATE, "Bearer")
                    exchange.response.setComplete()
                }
            }
            .cors {
                    cors -> cors.disable()
            }
            .csrf {it.disable()}
            .authorizeExchange { auth ->
                auth.pathMatchers("public/**").permitAll()
                    .anyExchange().authenticated()
            }
            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic {it.disable()}
            .formLogin {it.disable()}
        return http.build();
    }

}