package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.dto.CustomUserDetails
import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class MemberServiceImpl(
    private var memberRepository: MemberRepository, private var passwordEncoder: PasswordEncoder
) : MemberService {
    @Transactional
    @Throws(Exception::class)
    override fun join(request: MemberJoinRequest): Member {
        val savedMember = memberRepository.findById(request.email?:throw IllegalArgumentException("이메일이 누락되었습니다."))
        if (savedMember.isEmpty) {
            val member = request.toEntity(passwordEncoder)
            return memberRepository.save(member)
        } else {
            throw DuplicateKeyException("이미 사용중인 이메일입니다.")
        }
    }

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        println(username)
        username ?: throw IllegalArgumentException("username is null")
        val member: Member = memberRepository.findByEmailWithAuthority(username)
            ?: throw UsernameNotFoundException(username)
        return CustomUserDetails.from(member)
    }
}