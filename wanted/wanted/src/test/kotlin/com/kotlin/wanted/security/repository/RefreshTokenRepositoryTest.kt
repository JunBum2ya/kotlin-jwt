package com.kotlin.wanted.security.repository

import com.kotlin.wanted.security.entity.RefreshToken
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RefreshTokenRepositoryTest {
    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @BeforeEach
    fun createTestToken() {
        val refreshToken = RefreshToken(email = "test@tistory.com", token = "12323523546534674547")
        refreshTokenRepository.save(refreshToken)
    }

    @AfterEach
    fun deleteAllToken() {
        refreshTokenRepository.deleteAll()
    }

    @Test
    fun createRefreshToken() {
        val refreshToken = RefreshToken(email = "tistory@tistory.com", token = "1232352354653467457547")
        val savedRefreshToken = refreshTokenRepository.save(refreshToken)
        Assertions.assertEquals(refreshToken.getEmail(),savedRefreshToken.getEmail())
        Assertions.assertNotNull(refreshToken.getCreateAt())
        Assertions.assertNotNull(savedRefreshToken.getCreateBy())
        Assertions.assertNotNull(refreshToken.getModifiedAt())
        Assertions.assertNotNull(savedRefreshToken.getModifiedBy())
    }

    @Test
    fun findByEmail() {
        val refreshToken = refreshTokenRepository.findByEmail("test@tistory.com")
        Assertions.assertNotNull(refreshToken)
        refreshToken?.let {
            Assertions.assertEquals(it.getToken(),"12323523546534674547")
            Assertions.assertNotNull(it.getCreateAt())
            Assertions.assertNotNull(it.getCreateBy())
        }
    }

    @Test
    fun findByEmailAndToken() {
        val refreshToken = refreshTokenRepository.findByEmailAndToken("test@tistory.com","12323523546534674547")
        Assertions.assertNotNull(refreshToken)
        refreshToken?.let {
            Assertions.assertNotNull(it.getCreateAt())
            Assertions.assertNotNull(it.getCreateBy())
        }
    }

}