package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.dto.MemberUpdateRequest
import com.kotlin.wanted.member.entity.Member
import org.springframework.security.core.userdetails.UserDetailsService

interface MemberService : UserDetailsService {
    @Throws(Exception::class)
    fun join(request: MemberJoinRequest): Member
    fun updateMember(email: String, request: MemberUpdateRequest): Member
    fun deleteMember(email: String): Member
    fun clear()
    fun findByRefreshToken(refreshToken: String) : Member
}