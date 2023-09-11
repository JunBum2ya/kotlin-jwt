package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.member.entity.QAuthority
import com.kotlin.wanted.member.entity.QMember
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl : MemberRepositoryCustom {
    private lateinit var jpaQueryFactory: JPAQueryFactory;

    constructor(jpaQueryFactory: JPAQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
    override fun findByEmailWithAuthority(email: String): Member? {
        return jpaQueryFactory
            .selectFrom(QMember.member)
            .leftJoin(QMember.member.authorities, QAuthority.authority)
            .fetchFirst()
    }
}