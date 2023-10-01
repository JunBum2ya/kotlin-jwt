package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.dto.CustomUserDetails
import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.dto.MemberUpdateRequest
import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.member.repository.MemberRepository
import com.kotlin.wanted.security.component.TokenProvider
import jakarta.transaction.Transactional
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class MemberServiceImpl(
    private var memberRepository: MemberRepository, private var passwordEncoder: PasswordEncoder,private var tokenProvider: TokenProvider
) : MemberService {
    @Transactional
    @Throws(Exception::class)
    override fun join(request: MemberJoinRequest): Member {
        val savedMember = memberRepository.findById(request.email ?: throw IllegalArgumentException("이메일이 누락되었습니다."))
        if (savedMember.isEmpty) {
            val member = request.toEntity(passwordEncoder)
            val token = tokenProvider.createRefreshToken(member)
            member.updateToken(token)
            return memberRepository.save(member)
        } else {
            throw DuplicateKeyException("이미 사용중인 이메일입니다.")
        }
    }

    /**
     * 사용자 정보 수정
     */
    @Transactional
    override fun updateMember(email: String, request: MemberUpdateRequest): Member {
        val member = memberRepository.findById(email).orElseThrow { UsernameNotFoundException(email) }
        request.update(member = member, passwordEncoder = passwordEncoder)
        return member
    }

    @Transactional
    override fun deleteMember(email: String): Member {
        val member = memberRepository.findById(email).orElseThrow { UsernameNotFoundException(email) }
        memberRepository.delete(member)
        return member
    }

    @Transactional
    override fun clear() {
        memberRepository.deleteAll()
    }

    override fun findByRefreshToken(refreshToken: String): Member {
        TODO("Not yet implemented")
    }

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        val member: Member =
            memberRepository.findByEmailWithAuthority(username ?: throw IllegalArgumentException("username is null"))
                ?: throw UsernameNotFoundException(username)
        return CustomUserDetails.from(member)
    }
}