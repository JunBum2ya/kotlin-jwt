package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Authority
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorityRepository : JpaRepository<Authority,String> {
}