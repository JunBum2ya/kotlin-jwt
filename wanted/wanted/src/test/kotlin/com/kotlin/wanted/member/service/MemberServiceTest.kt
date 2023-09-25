package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.entity.Member
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    private var member : Member? = null

    @BeforeEach
    @Transactional
    fun initMember() {
        val request : MemberJoinRequest = MemberJoinRequest("tistory@naver.com","1234", listOf( "ADMIN","MEMBER","GUEST" ))
        val savedMember = memberService.join(request)
    }

    @Test
    @Transactional
    fun join() {
        val request : MemberJoinRequest = MemberJoinRequest("tistory2@naver.com","1234",listOf( "ADMIN","MEMBER","GUEST" ))
        val savedMember = memberService.join(request)
        Assertions.assertEquals(savedMember.getEmail(),request.email)
    }

    @Test
    @Transactional
    fun loadUserByUsername() {
        val member = memberService.loadUserByUsername("tistory@naver.com")
        Assertions.assertEquals(member.authorities.size,3)
        Assertions.assertEquals(member.username,"tistory@naver.com")
        Assertions.assertNotEquals(member.password,"1234")
    }

}