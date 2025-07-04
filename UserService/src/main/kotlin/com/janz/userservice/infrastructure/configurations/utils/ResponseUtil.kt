package com.janz.userservice.infrastructure.configurations.utils

import com.janz.userservice.infrastructure.configurations.entities.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object ResponseUtil {

    fun <T> ok(data: T? = null, message: String = "OK"): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.ok(
            ApiResponse.Builder<T>()
                .success(true)
                .message(message)
                .data(data)
                .build()
        )
    }

    fun <T> success(
        status: HttpStatus = HttpStatus.OK, message: String = "OK", data: T? = null
    ):ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(status)
            .body(ApiResponse.Builder<T>().success(true)
                .message(message).data(data).build())
    }

    fun <T> error(message: String, status: HttpStatus = HttpStatus.BAD_REQUEST, data: T? = null): ResponseEntity<ApiResponse<T>> {
        return ResponseEntity.status(status).body(
            ApiResponse.Builder<T>()
                .success(false)
                .message(message)
                .data(data)
                .build()
        )
    }

    fun <T> errorWithData(message: String, status: HttpStatus, data: T): ResponseEntity<ApiResponse<T>> {
        return error(message, status, data)
    }
}