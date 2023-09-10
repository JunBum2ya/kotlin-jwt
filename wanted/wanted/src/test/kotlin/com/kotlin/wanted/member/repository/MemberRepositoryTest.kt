package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private lateinit var memberRepository : MemberRepository

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    var testMember : Member? = null;

    @BeforeEach
    public fun initTestMember() {
        val authority1 = Authority("auth1","test1")
        val authority2 = Authority("auth2","test2")
        val authorityList : MutableList<Authority> = mutableListOf(authority1,authority2)
        authorityRepository.saveAll(authorityList)
        testMember = Member(email = "test@tistory.com", password = "1234", authorityList)
        memberRepository.save(testMember!!)
    }
    //멤버 create
    @Test
    fun saveTest() {
        val member = Member(email = "happybrother@tistory.com", password = "1234", mutableListOf());
        val savedMember : Member = memberRepository.save(member)
        Assertions.assertEquals(member.getEmail(),savedMember.getEmail())
    }
    //단건 조회
    @Test
    @Transactional
    fun findByEmail() {
        val savedMember : Member = memberRepository.findById(testMember?.getEmail()!!).orElseThrow()
        Assertions.assertEquals(savedMember.getEmail(),testMember?.getEmail())
        Assertions.assertEquals(savedMember.getAuthorities().size,2)
    }
    //멤버 update
    @Test
    @Transactional
    fun updateMember() {
        memberRepository.findById(testMember?.getEmail()!!)?.let {
            m -> m.get().update("14564", mutableListOf())
        }
        memberRepository.findById(testMember?.getEmail()!!)?.let {
            Assertions.assertEquals(it.get().getPassword(),"14564")
        }
    }
    //멤버 삭제
    @Test
    @Transactional
    fun removeMember() {
        testMember?.let { memberRepository.delete(it) }
        Assertions.assertTrue(memberRepository.findById(testMember?.getEmail()!!).isEmpty)
    }

}