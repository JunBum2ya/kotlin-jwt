package com.kotlin.wanted.security.repository

import com.kotlin.wanted.security.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

//todo redis로 변경필요
interface RefreshTokenRepository : JpaRepository<RefreshToken,Long> {
    fun findByEmail(email : String) : RefreshToken? //email을 기준으로 refreshToken 검색
    fun findByEmailAndToken(email : String,token : String) : RefreshToken? //email과 refreshToken으로 검색
}