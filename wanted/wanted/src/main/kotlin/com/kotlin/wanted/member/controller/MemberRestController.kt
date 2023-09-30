package com.kotlin.wanted.member.controller

import com.kotlin.wanted.member.dto.*
import com.kotlin.wanted.member.service.MemberService
import com.kotlin.wanted.security.component.TokenProvider
import com.kotlin.wanted.security.filter.CustomJwtFilter
import com.sun.net.httpserver.Headers
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberRestController(
    val memberService: MemberService,
    val authenticationManagerBuilder: AuthenticationManagerBuilder,
    val tokenProvider: TokenProvider
) {
    @PostMapping("/member/login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<MemberLoginResponse> {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.createToken(authentication)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer $jwt")
        return ResponseEntity.ok(
            MemberLoginResponse(
                result = true,
                email = authentication.name,
                token = jwt,
                message = "로그인 성공"
            )
        )
    }

    @PostMapping("/member/join")
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<MemberJoinResponse> {
        val member = memberService.join(request)
        return ResponseEntity.ok(MemberJoinResponse(email = member.getEmail(), result = true, message = "success join"))
    }

    @GetMapping("/member/test")
    fun test(authentication: Authentication?) : ResponseEntity<String> {
        println(authentication?.name)
        return ResponseEntity.ok("test complete")
    }

}