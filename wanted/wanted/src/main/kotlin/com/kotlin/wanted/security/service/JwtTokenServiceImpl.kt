package com.kotlin.wanted.security.service

import com.kotlin.wanted.member.entity.RefreshToken
import com.kotlin.wanted.security.component.TokenProvider
import com.kotlin.wanted.security.dto.JwtToken
import com.kotlin.wanted.security.dto.TokenSaveParameter
import com.kotlin.wanted.security.filter.CustomJwtFilter
import com.kotlin.wanted.security.repository.RefreshTokenRepository
import jakarta.transaction.Transactional
import org.springframework.http.HttpHeaders
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class JwtTokenServiceImpl(
    val refreshTokenRepository: RefreshTokenRepository,
    val tokenProvider: TokenProvider,
    val authenticationManagerBuilder: AuthenticationManagerBuilder
) : JwtTokenService {
    @Transactional
    override fun createAccessToken(refreshToken: String): String {
        if (tokenProvider.validateToken(refreshToken)) {
            val authentication = tokenProvider.getAuthentication(refreshToken)
            val tokenEntity =
                refreshTokenRepository.findByEmailAndToken(email = authentication.name, token = refreshToken) //refresh token 확인
            tokenEntity?.let {
                if (tokenProvider.validateToken(it.getToken())) {
                    return tokenProvider.createAccessToken(authentication) //존재할 경우 access token 생성
                }
            }
        }
        throw AccessDeniedException("refresh token을 재발급해주세요.")
    }

    @Transactional
    override fun saveRefreshToken(parameter: TokenSaveParameter): JwtToken {
        val jwtToken = tokenProvider.createToken(parameter)
        val tokenEntity = refreshTokenRepository.findByEmail(parameter.email)
        if (tokenEntity != null) {
            tokenEntity.update(jwtToken.refreshToken)
        } else {
            val refreshToken = RefreshToken(email = parameter.email, jwtToken.refreshToken)
            refreshTokenRepository.save(refreshToken)
        }
        return jwtToken
    }

    @Transactional
    override fun discardRefreshToken(email: String) {
        val refreshToken = refreshTokenRepository.findByEmail(email = email)
        refreshToken?.let {
            refreshTokenRepository.delete(it)
        }
    }

    override fun setAuthentication(accessToken: String): Authentication {
        val authentication = tokenProvider.getAuthentication(accessToken)
        SecurityContextHolder.getContext().authentication = authentication
        val httpHeaders = HttpHeaders()
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer $accessToken")
        return authentication
    }

    override fun reissueRefreshToken(email: String, password: String): JwtToken {
        val authenticationToken = UsernamePasswordAuthenticationToken(email, password)
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication = authentication
        val jwtToken = saveRefreshToken(TokenSaveParameter.from(authentication))
        val httpHeaders = HttpHeaders()
        httpHeaders.add(CustomJwtFilter.AUTHORIZATION_HEADER, "Bearer ${jwtToken.accessToken}")
        val refreshToken = refreshTokenRepository.findByEmail(email = email)
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken)
        }
        refreshTokenRepository.save(RefreshToken(email = email, jwtToken.refreshToken))
        return jwtToken
    }

}