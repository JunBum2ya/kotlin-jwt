package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Member

interface MemberRepositoryCustom {
    fun findByEmailWithAuthority(email : String) : Member?
}