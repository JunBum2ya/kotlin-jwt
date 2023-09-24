package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.entity.Member
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

interface MemberService : UserDetailsService {
    @Throws(Exception::class)
    fun join(request: MemberJoinRequest): Member
}