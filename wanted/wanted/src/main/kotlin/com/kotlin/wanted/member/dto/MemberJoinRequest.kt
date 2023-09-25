package com.kotlin.wanted.member.dto

import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import java.util.stream.Collectors

class MemberJoinRequest(
    @field:NotNull(message = "이메일을 입력하세요.") @field:Email(message = "이메일 양식을 입력하세요.") var email: String?,
    @field:NotNull(message = "패스워드를 입력하세요.") @field:Size(
        min = 8,
        message = "패스워드를 8자리 이상 입력하세요."
    ) var password: String?,
    var authorities: List<String>?
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        val authorityEntityList = this.authorities?.let {
            it.stream()
                .map { authority -> Authority(name = authority, "") }
                .collect(Collectors.toList())
        } ?: mutableListOf()
        return Member(email ?: "", passwordEncoder.encode(password), authorityEntityList)
    }

}