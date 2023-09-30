package com.kotlin.wanted.member.dto

import com.kotlin.wanted.member.code.Gender
import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import jakarta.validation.constraints.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.stream.Collectors

class MemberUpdateRequest(
    @field:NotNull(message = "패스워드를 입력하세요.") @field:Size(
        min = 8,
        message = "패스워드를 8자리 이상 입력하세요."
    ) var password: String?,
    var authorities: List<String>?,
    @field:Pattern(regexp = "^MALE|FEMALE|NONE_BINARY\$", message = "잘못된 데이터입니다.") var gender: String?,
    @field:Min(value = 0, message = "나이는 0보다 커야 합니다.") var age: Int?,
    @field:Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}\$", message = "전화번호 형식에 맞게 입력해주세요.") var phoneNumber: String?
) {
    fun update(member: Member, passwordEncoder: PasswordEncoder): Member {
        val authorityEntityList = this.authorities?.let {
            it.stream()
                .map { authority -> Authority(name = authority, "") }
                .collect(Collectors.toList())
        } ?: mutableListOf()
        member.update(
            password = passwordEncoder.encode(password),
            authorityList = authorityEntityList,
            gender = gender?.let { Gender.valueOf(it) },
            age = age,
            phoneNumber = phoneNumber
        )
        return member
    }
}