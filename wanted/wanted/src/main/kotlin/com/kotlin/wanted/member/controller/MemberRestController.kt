package com.kotlin.wanted.member.controller

import com.kotlin.wanted.member.dto.*
import com.kotlin.wanted.member.entity.RefreshToken
import com.kotlin.wanted.member.service.MemberService
import com.kotlin.wanted.security.component.TokenProvider
import com.kotlin.wanted.security.dto.TokenSaveParameter
import com.kotlin.wanted.security.filter.CustomJwtFilter
import com.kotlin.wanted.security.service.JwtTokenService
import com.sun.net.httpserver.Headers
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
class MemberRestController(
    val memberService: MemberService,
    val jwtTokenService: JwtTokenService,
) {
    /**
     * refesh token,access token 모두 발급
     */
    @PostMapping("/member/login")
    fun login(@Valid @RequestBody request: MemberLoginRequest): ResponseEntity<MemberLoginResponse> {
        val jwtToken = jwtTokenService.reissueRefreshToken(
            request.email ?: throw IllegalArgumentException("email is null"),
            request.password ?: throw IllegalArgumentException("password is null")
        )
        return ResponseEntity.ok(
            MemberLoginResponse(
                result = true,
                email = jwtToken.username,
                accessToken = jwtToken.accessToken,
                refreshToken = jwtToken.refreshToken,
                message = "로그인 성공"
            )
        )
    }

    @PostMapping("/member/issue-token")
    fun reissueAccessToken(@RequestBody request: MemberIssueAccessTokenRequest): ResponseEntity<MemberIssueAccessToken> {
        val jwtToken = jwtTokenService.createAccessToken(request.refreshToken)
        jwtTokenService.setAuthentication(jwtToken)
        return ResponseEntity.ok(
            MemberIssueAccessToken(
                result = true,
                accessToken = jwtToken,
                message = "토큰이 재발급되었습니다."
            )
        )
    }

    /**
     * refresh token 삭제
     */
    @DeleteMapping("/member/discard-token")
    fun discardRefreshToken(authentication: Authentication?): ResponseEntity<MemberDiscardRefreshTokenResponse> {
        authentication?.let {
            return ResponseEntity.ok(MemberDiscardRefreshTokenResponse(result = true, email = authentication.name, message = "refresh token을 폐기했습니다"))
        } ?: throw AccessDeniedException("로그인을 하지 않았습니다.")
    }

    /**
     * refesh token,access token 모두 발급
     */
    @PostMapping("/member/join")
    fun join(@Valid @RequestBody request: MemberJoinRequest): ResponseEntity<MemberJoinResponse> {
        val member = memberService.join(request)
        val jwtToken = jwtTokenService.saveRefreshToken(TokenSaveParameter.from(member))
        jwtTokenService.setAuthentication(jwtToken.accessToken)
        return ResponseEntity.ok(
            MemberJoinResponse(
                email = jwtToken.username,
                result = true,
                refreshToken = jwtToken.refreshToken,
                accessToken = jwtToken.accessToken,
                message = "success join"
            )
        )
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