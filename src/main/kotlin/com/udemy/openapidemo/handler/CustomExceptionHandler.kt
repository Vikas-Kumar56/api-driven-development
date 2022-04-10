package com.udemy.openapidemo.handler

import com.udemy.openapidemo.models.Error
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

sealed class ApiException(msg: String, val code: Int): RuntimeException(msg)

class NotFoundException(msg: String, code:Int = HttpStatus.NOT_FOUND.value()): ApiException(msg, code)


@ControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(value = [ApiException::class])
    fun onApiException(ex: ApiException): ResponseEntity<Error> {
        return ResponseEntity.status(ex.code).body(Error(
            code = ex.code,
            message = ex.message
        ))
    }
}