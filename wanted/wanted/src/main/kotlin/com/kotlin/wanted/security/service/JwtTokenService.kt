package com.kotlin.wanted.security.service

import com.kotlin.wanted.security.dto.JwtToken
import com.kotlin.wanted.security.dto.TokenSaveParameter
import org.springframework.security.core.Authentication

interface JwtTokenService {
    fun createAccessToken(refreshToken : String) : String //refresh token으로 access token 생성
    fun saveRefreshToken(parameter: TokenSaveParameter) : JwtToken //refresh token을 새로 저장하거나 update
    fun discardRefreshToken(email : String) //refresh token 폐기
    fun setAuthentication(accessToken : String) : Authentication //SecurityContext에 Authentication 추가
    fun reissueRefreshToken(email : String, password : String) : JwtToken //refresh token 재발급
}