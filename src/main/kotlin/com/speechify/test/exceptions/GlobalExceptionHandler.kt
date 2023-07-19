package com.speechify.test.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RedisException::class)
    @ResponseBody
    fun handleRedisException(ex: RedisException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Internal Server Error", ex.message ?: "An unexpected error occurred")
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorResponse)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleGlobalException(ex: Exception): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse("Internal Server Error", ex.message ?: "An unexpected error occurred")
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }

    data class ErrorResponse(val error: String, val message: String)
}