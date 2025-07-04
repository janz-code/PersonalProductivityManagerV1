package com.janz.userservice.infrastructure.configurations

import com.janz.userservice.infrastructure.configurations.entities.ApiResponse
import com.janz.userservice.infrastructure.configurations.utils.ResponseUtil
import io.jsonwebtoken.ExpiredJwtException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("Invalid argument: ${ex.message}", HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Map<String, List<String>>>> {
        val errorsMap = ex.bindingResult.fieldErrors
            .groupBy { it.field }
            .mapValues { (_, fieldErrors) ->
                fieldErrors.mapNotNull { it.defaultMessage }
            }

        return ResponseUtil.errorWithData(
            message = "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            data = errorsMap
        )
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleWebFluxValidationError(ex: WebExchangeBindException): ResponseEntity<ApiResponse<Map<String, List<String>>>> {
        val errorsMap = ex.fieldErrors
            .groupBy { it.field }
            .mapValues { (_, fieldErrors) -> fieldErrors.mapNotNull { it.defaultMessage } }

        return ResponseUtil.error(
            message = "Validation failed",
            status = HttpStatus.BAD_REQUEST,
            data = errorsMap
        )
    }

    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInput(ex: ServerWebInputException): ResponseEntity<ApiResponse<Map<String, List<String>>>> {
        val message = ex.mostSpecificCause.message.orEmpty()

        val fieldErrors = mutableMapOf<String, List<String>>()

        val match = Regex("""(?<=\[\\?")[\w]+(?="])""").find(message)
        val field = match?.value ?: "unknown"

        val cleanMsg = when {
            "missing" in message.lowercase() || "null" in message.lowercase() -> "Field is required"
            "Cannot deserialize" in message -> "Invalid data type"
            else -> "Invalid value"
        }

        fieldErrors[field] = listOf(cleanMsg)

        return ResponseUtil.error(
            message = "Invalid request body",
            status = HttpStatus.BAD_REQUEST,
            data = fieldErrors
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadableHttpMessage(ex: HttpMessageNotReadableException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error(
            "Malformed JSON request or incorrect types: ${ex.mostSpecificCause.message}",
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(Exception::class) // ⬅️ must be last!
    fun handleGenericException(ex: Exception): ResponseEntity<ApiResponse<Nothing>> {
        logger.error("Unhandled exception", ex)
        return ResponseUtil.error("Unexpected error: ${ex.message}", HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(SecurityException::class)
    fun handleSecurityException(ex: SecurityException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("Authentication error: ${ex.message}", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException::class)
    fun handleAuthenticationCredentialsNotFoundException(ex: AuthenticationCredentialsNotFoundException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("Authentication error: ${ex.message}", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AuthorizationDeniedException::class)
    fun handleAuthorizationDeniedException(ex: AuthorizationDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("You don't have permissions to access", HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(ex: ExpiredJwtException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("Token expired: ${ex.message}", HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(ex: AccessDeniedException): ResponseEntity<ApiResponse<Nothing>> {
        return ResponseUtil.error("Access denied: ${ex.message}", HttpStatus.FORBIDDEN)
    }

}