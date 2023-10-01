package com.kotlin.wanted.member.dto

import com.kotlin.wanted.member.code.Gender
import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
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
    var authorities: List<String>?,
    @field:Pattern(regexp = "^MALE|FEMALE|NONE_BINARY\$", message = "잘못된 데이터입니다.") var gender: String?,
    @field:Min(value = 0, message = "나이는 0보다 커야 합니다.") var age: Int?,
    @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$", message = "전화번호 형식에 맞게 입력해주세요.") var phoneNumber: String?
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        val authorityEntityList = this.authorities?.let {
            it.stream()
                .map { authority -> Authority(name = authority, "") }
                .collect(Collectors.toList())
        } ?: mutableListOf()
        return Member(
            email = email ?: "",
            password = passwordEncoder.encode(password),
            authorities = authorityEntityList,
            gender = gender?.let { Gender.valueOf(it) },
            age = age,
            phoneNumber = phoneNumber,
            token = null
        )
    }

}