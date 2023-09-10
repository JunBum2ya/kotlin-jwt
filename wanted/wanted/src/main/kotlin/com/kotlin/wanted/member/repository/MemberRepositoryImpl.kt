package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Member
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
            .selectFrom<Member>(QMember.member)
            .leftJoin(QMember.member.authorityList, QAuthority.authority)
            .fetchFirst()
    }
}