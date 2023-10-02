package com.kotlin.wanted.member.dto

import com.kotlin.wanted.member.entity.RefreshToken

data class MemberIssueAccessTokenRequest(val refreshToken: String)