package com.kotlin.wanted.security.dto

data class JwtToken(val username : String,val refreshToken : String, val accessToken : String)