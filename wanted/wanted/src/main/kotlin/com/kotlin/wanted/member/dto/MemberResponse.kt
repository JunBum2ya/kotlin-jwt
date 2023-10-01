package com.kotlin.wanted.member.dto

data class MemberLoginResponse(val result: Boolean, val email: String, val refreshToken : String, val accessToken : String, val message: String)
data class MemberJoinResponse(val result: Boolean, val email: String, val refreshToken : String, val accessToken : String, val message: String)
data class MemberUpdateResponse(val result: Boolean, val email: String, val message: String)
data class MemberDeleteResponse(val result: Boolean, val email: String, val message: String)
data class MemberIssueAccessToken(val result: Boolean,val email: String,val accessToken: String, val message: String)