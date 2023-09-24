package com.kotlin.wanted.global.handler

import com.kotlin.wanted.global.dto.ErrorResponse
import com.kotlin.wanted.security.filter.CustomJwtFilter
import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException): ResponseEntity<ErrorResponse> {
        logger.error(e.message)
        return ResponseEntity(ErrorResponse(code = "200", message = "이미 키가 테이블에 존재합니다."), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.error(e.message)
        val message = e.allErrors.stream().map { error -> error.defaultMessage }.collect(Collectors.joining("|"))
        return ResponseEntity(ErrorResponse(code = "201",message = message),HttpStatus.BAD_REQUEST)
    }
}