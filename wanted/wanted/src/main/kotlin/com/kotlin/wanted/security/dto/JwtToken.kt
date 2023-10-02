package com.kotlin.wanted.security.dto

import com.kotlin.wanted.member.entity.RefreshToken

data class JwtToken(val username : String,val refreshToken : String, val accessToken : String)