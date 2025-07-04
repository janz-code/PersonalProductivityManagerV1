package com.janz.userservice.infrastructure.configurations.entities

class ApiResponse<T> private constructor(
    val success: Boolean,
    val message: String,
    val data: T?
) {
    class Builder<T> {
        private var success: Boolean = true
        private var message: String = ""
        private var data: T? = null

        fun success(success: Boolean) = apply { this.success = success }
        fun message(message: String) = apply { this.message = message }
        fun data(data: T?) = apply { this.data = data }

        fun build(): ApiResponse<T> = ApiResponse(success, message, data)
    }
}