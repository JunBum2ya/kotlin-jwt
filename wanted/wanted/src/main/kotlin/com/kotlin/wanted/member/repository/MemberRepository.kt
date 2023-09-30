package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, String>, MemberRepositoryCustom {
}