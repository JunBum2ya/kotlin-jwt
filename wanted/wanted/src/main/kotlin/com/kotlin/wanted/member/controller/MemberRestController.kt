package com.kotlin.wanted.member.controller

import com.kotlin.wanted.member.dto.*
import com.kotlin.wanted.member.entity.RefreshToken
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
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberRestController(
    val memberService: MemberService,
    val authenticationManagerBuilder: AuthenticationManagerBuilder,
    val tokenProvider: TokenProvider
) {
    /**
     * refesh token,access token 모두 발급
     */
    @PostMapping("/member/login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<MemberLoginResponse> {
        val authenticationToken = UsernamePasswordAuthenticationToken(request.email, request.password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val refreshToken = tokenProvider.createRefreshToken(authentication)
        val accessToken = tokenProvider.createAccessToken(authentication)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer $accessToken")
        return ResponseEntity.ok(
            MemberLoginResponse(
                result = true,
                email = authentication.name,
                accessToken = accessToken,
                refreshToken = refreshToken,
                message = "로그인 성공"
            )
        )
    }

    @GetMapping("/member/issue-token")
    fun reissueAccessToken(refreshToken: RefreshToken): ResponseEntity<MemberIssueAccessToken> {
        return ResponseEntity.ok(null)
    }

    /**
     * refesh token,access token 모두 발급
     */
    @PostMapping("/member/join")
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<MemberJoinResponse> {
        val member = memberService.join(request)
        member.getToken()?.let {
            val refreshToken = it.getToken()
            val authentication = tokenProvider.getAuthentication(refreshToken)
            SecurityContextHolder.getContext().authentication = authentication
            val accessToken = tokenProvider.createAccessToken(authentication)
            return ResponseEntity.ok(
                MemberJoinResponse(
                    email = member.getEmail(),
                    result = true,
                    refreshToken = refreshToken,
                    accessToken = accessToken,
                    message = "success join"
                )
            )
        }?:throw Exception("리프레시 토큰이 생성되지 않았습니다.")
    }

    @PutMapping("/member/update")
    fun updateMember(
        @Valid @RequestBody request: MemberUpdateRequest,
        authentication: Authentication?
    ): ResponseEntity<MemberUpdateResponse> {
        authentication?.let {
            val member = memberService.updateMember(it.name, request)
            return ResponseEntity.ok(
                MemberUpdateResponse(
                    result = true,
                    email = member.getEmail(),
                    message = "회원 정보가 변경되었습니다."
                )
            )
        } ?: throw UsernameNotFoundException("인증되지 않은 사용자입니다.")
    }

    @DeleteMapping("/member/delete")
    fun deleteMember(authentication: Authentication?): ResponseEntity<MemberDeleteResponse> {
        authentication?.let {
            val member = memberService.deleteMember(authentication.name)
            return ResponseEntity.ok(
                MemberDeleteResponse(
                    result = true,
                    email = member.getEmail(),
                    message = "삭제되었습니다."
                )
            )
        } ?: throw UsernameNotFoundException("인증되지 않은 사용자입니다.")
    }

    @GetMapping("/member/info")
    fun info(authentication: Authentication?): ResponseEntity<String> {
        return ResponseEntity.ok("test complete")
    }

}