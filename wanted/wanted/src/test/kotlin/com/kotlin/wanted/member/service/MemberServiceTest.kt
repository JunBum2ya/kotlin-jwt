package com.kotlin.wanted.member.service

import com.kotlin.wanted.member.code.Gender
import com.kotlin.wanted.member.dto.MemberJoinRequest
import com.kotlin.wanted.member.dto.MemberUpdateRequest
import com.kotlin.wanted.member.entity.Member
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.userdetails.UsernameNotFoundException

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private lateinit var memberService: MemberService

    private var member: Member? = null

    @BeforeEach
    @Transactional
    fun initMember() {
        println("initMember")
        val request: MemberJoinRequest = MemberJoinRequest(
            "tistory@naver.com",
            "1234",
            listOf("ADMIN", "MEMBER", "GUEST"),
            gender = "MALE",
            age = 30,
            phoneNumber = "010-1234-5678"
        )
        val savedMember = memberService.join(request)
    }

    @AfterEach
    @Transactional
    fun deleteMember() {
        memberService.clear()
    }

    @Test
    @Transactional
    fun join() {
        val request: MemberJoinRequest = MemberJoinRequest(
            "tistory2@naver.com",
            "1234",
            listOf("ADMIN", "MEMBER", "GUEST"),
            gender = null,
            age = null,
            phoneNumber = null
        )
        val savedMember = memberService.join(request)
        Assertions.assertEquals(savedMember.getEmail(), request.email)
    }

    @Test
    @Transactional
    fun loadUserByUsername() {
        val member = memberService.loadUserByUsername("tistory@naver.com")
        Assertions.assertEquals(member.authorities.size, 3)
        Assertions.assertEquals(member.username, "tistory@naver.com")
        Assertions.assertNotEquals(member.password, "1234")
    }

    @Test
    @Transactional
    fun updateMember() {
        val request = MemberUpdateRequest(
            password = "test123456789",
            authorities = listOf("ADMIN"),
            gender = "MALE",
            age = 20,
            phoneNumber = "010-2456-4564"
        )
        val updateMember = memberService.updateMember("tistory@naver.com", request)
        Assertions.assertEquals(updateMember.getGender().toString(), "MALE")
    }

    @Test
    @Transactional
    fun testDeleteMember() {
        val deletedMember = memberService.deleteMember("tistory@naver.com")
        try {
            memberService.loadUserByUsername("tistory@naver.com")
            throw Exception("삭제 실패")
        } catch (e: UsernameNotFoundException) {
            Assertions.assertTrue(true)
        }
    }

}