package com.yapp.muckpot.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.yapp.muckpot.common.ResponseDto
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.valueOf
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.validation.ValidationException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = KLogging().logger

    @ExceptionHandler(MuckPotException::class)
    fun muckpotGlobalExceptionHandler(exception: MuckPotException): ResponseEntity<ResponseDto> {
        log.error(exception) { "" }
        val responseDto = exception.errorCode.toResponseDto()
        return ResponseEntity.status(valueOf(responseDto.status)).body(responseDto)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun badRequestErrorHandler(exception: Exception): ResponseEntity<ResponseDto> {
        log.error(exception) { "" }
        return ResponseEntity.badRequest()
            .body(ResponseDto(HttpStatus.BAD_REQUEST.value(), exception.message))
    }

    @ExceptionHandler(value = [MethodArgumentNotValidException::class, ValidationException::class])
    fun methodArgumentNotValidExceptionHandler(exception: Exception): ResponseEntity<ResponseDto> {
        log.error(exception) { "" }
        var message = exception.message
        if (exception is MethodArgumentNotValidException && exception.hasErrors()) {
            message = exception.allErrors.firstOrNull()?.defaultMessage ?: exception.message
        }
        return ResponseEntity.badRequest()
            .body(ResponseDto(HttpStatus.BAD_REQUEST.value(), message))
    }

    @ExceptionHandler(value = [HttpMessageNotReadableException::class])
    fun httpMessageNotReadableExceptionHandler(exception: HttpMessageNotReadableException): ResponseEntity<ResponseDto> {
        log.error(exception) { "" }
        var message = exception.message
        val cause = exception.cause
        if (cause is MissingKotlinParameterException) {
            val name = cause.parameter.name
            message = "{$name}값이 필요합니다."
        } else if (cause is InvalidFormatException) {
            message = "${cause.value}은(는) 유효하지 않은 포맷입니다."
        }
        return ResponseEntity.badRequest()
            .body(ResponseDto(HttpStatus.BAD_REQUEST.value(), message))
    }

    @ExceptionHandler(Exception::class)
    fun internalServerErrorHandler(exception: Exception): ResponseEntity<ResponseDto> {
        log.error(exception) { "" }
        return ResponseEntity.internalServerError()
            .body(ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.message))
    }
}
