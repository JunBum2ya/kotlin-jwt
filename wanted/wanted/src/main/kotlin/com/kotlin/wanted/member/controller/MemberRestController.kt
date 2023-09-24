package com.kotlin.wanted.member.controller

import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.dto.MemberJoinResponse
import com.kotlin.wanted.member.service.MemberService
import com.kotlin.wanted.security.component.TokenProvider
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
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

    @PostMapping("/member/join")
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<MemberJoinResponse> {
        val member = memberService.join(request)
        return ResponseEntity.ok(MemberJoinResponse(email = member.getEmail(), result = true, message = "success join"))
    }

}