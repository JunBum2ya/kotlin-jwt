package com.kotlin.wanted.security.dto

import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.member.entity.RefreshToken
import org.springframework.security.core.Authentication
import java.util.stream.Collector
import java.util.stream.Collectors

data class TokenSaveParameter(val email : String, val authorities : List<String>) {
    companion object {
        fun from(member: Member) : TokenSaveParameter {
            val authorities = member.getAuthorities().stream().map { it.getName() }.collect(Collectors.toList())
            return TokenSaveParameter(email = member.getEmail(),authorities = authorities)
        }
        fun from(authentication : Authentication) : TokenSaveParameter {
            val authorities = authentication.authorities.stream().map { it.authority }.collect(Collectors.toList())
            return TokenSaveParameter(email = authentication.name, authorities = authorities)
        }
    }
}