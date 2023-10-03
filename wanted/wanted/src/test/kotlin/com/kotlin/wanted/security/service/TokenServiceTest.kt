package com.kotlin.wanted.security.service

import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.member.repository.MemberRepository
import com.kotlin.wanted.security.component.TokenProvider
import com.kotlin.wanted.security.dto.JwtToken
import com.kotlin.wanted.security.dto.TokenSaveParameter
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class TokenServiceTest {
    @Autowired
    private lateinit var jwtTokenService: JwtTokenService

    @Autowired
    private lateinit var tokenProvider: TokenProvider

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private var jwtToken: JwtToken? = null

    @BeforeEach
    fun initJwtToken() {
        jwtToken = jwtTokenService.saveRefreshToken(
            TokenSaveParameter(
                email = "test@tistory.com",
                authorities = listOf("ADMIN", "MEMBER")
            )
        )
    }

    @BeforeEach
    fun initMember() {
        val authority = Authority(name = "ADMIN", comment = "")
        val member = Member(
            email = "test@tistory.com",
            password = passwordEncoder.encode("123456789"),
            authorities = mutableListOf(authority),
            gender = null,
            age = null,
            phoneNumber = null
        )
        memberRepository.save(member)
    }

    @AfterEach
    fun deleteMember() {
        memberRepository.deleteAll()
    }

    @Test
    fun createAccessToken() {
        jwtToken?.let {
            val accessToken = jwtTokenService.createAccessToken(it.refreshToken)
            Assertions.assertNotNull(accessToken)
        }
    }

    @Test
    fun saveRefreshToken() {
        val savedToken = jwtTokenService.saveRefreshToken(
            TokenSaveParameter(
                email = "test@tistory.com",
                authorities = listOf("ADMIN", "MEMBER")
            )
        )
        Assertions.assertNotNull(savedToken)
        Assertions.assertTrue(tokenProvider.validateToken(savedToken.refreshToken))
        Assertions.assertTrue(tokenProvider.validateToken(savedToken.accessToken))
    }

    @Test
    fun discardRefreshToken() {
        jwtTokenService.discardRefreshToken("test@tistory.com")
        jwtToken?.let {
            Assertions.assertThrows(AccessDeniedException::class.java) { jwtTokenService.createAccessToken(it.refreshToken) }
        }
    }

    @Test
    fun setAuthentication() {
        jwtToken?.let {
            val authentication = jwtTokenService.setAuthentication(it.accessToken)
            val contextAuthentication = SecurityContextHolder.getContext().authentication
            Assertions.assertNotNull(contextAuthentication)
            Assertions.assertTrue(contextAuthentication.isAuthenticated)
            Assertions.assertEquals(contextAuthentication.name, authentication.name)
        }
    }

    @Test
    fun reissueRefreshToken() {
        val newJwtToken = jwtTokenService.reissueRefreshToken(
            email = "test@tistory.com",
            password = "123456789"
        )
        Assertions.assertTrue(tokenProvider.validateToken(newJwtToken.refreshToken))
        Assertions.assertTrue(tokenProvider.validateToken(newJwtToken.accessToken))
        val contextAuthentication = SecurityContextHolder.getContext().authentication
        Assertions.assertNotNull(contextAuthentication)
        Assertions.assertTrue(contextAuthentication.isAuthenticated)
        Assertions.assertEquals(contextAuthentication.name, "test@tistory.com")
    }

}