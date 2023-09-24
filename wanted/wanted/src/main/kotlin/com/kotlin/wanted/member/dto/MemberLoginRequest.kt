package com.kotlin.wanted.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class MemberLoginRequest(
    @NotNull(message = "이메일을 입력하세요.") @Email(message = "이메일 양식을 입력하세요.") val email: String,
    @NotNull(message = "패스워드를 입력하세요.") @Size(min = 8, message = "패스워드를 8자리 이상 입력하세요.") val password: String
)