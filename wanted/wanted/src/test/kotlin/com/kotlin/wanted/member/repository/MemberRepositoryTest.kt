package com.kotlin.wanted.member.repository

import com.kotlin.wanted.member.code.Gender
import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var authorityRepository: AuthorityRepository

    var testMember: Member? = null;

    @BeforeEach
    fun initTestMember() {
        val authority1 = Authority("auth1", "test1")
        val authority2 = Authority("auth2", "test2")
        val authorityList: MutableList<Authority> = mutableListOf(authority1, authority2)
        authorityRepository.saveAll(authorityList)
        testMember = Member(
            email = "test@tistory.com",
            password = "1234",
            authorityList,
            age = 30,
            gender = Gender.MALE,
            phoneNumber = "010-1234-5678"
        )
        memberRepository.save(testMember!!)
    }

    @AfterEach
    fun deleteMember() {
        memberRepository.deleteAll()
    }

    //멤버 create
    @Test
    fun saveTest() {
        val member = Member(
            email = "happybrother@tistory.com",
            password = "1234",
            authorities = mutableListOf(),
            age = null,
            gender = null,
            phoneNumber = null
        );
        val savedMember: Member = memberRepository.save(member)
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail())
    }

    //단건 조회
    @Test
    @Transactional
    fun findByEmail() {
        memberRepository.findByEmailWithAuthority(testMember?.getEmail()!!)
            ?.let { member: Member ->
                {
                    Assertions.assertEquals(member.getEmail(), testMember?.getEmail())
                    Assertions.assertEquals(member.getAuthorities().size, 2)
                }
            }
    }

    //멤버 update
    @Test
    @Transactional
    fun updateMember() {
        val member = testMember ?: return
        memberRepository.findByIdOrNull(
                member.getEmail()
            )?.update(
            password = "14564",
            authorityList = mutableListOf(),
            gender = Gender.FEMALE,
            age = 20,
            phoneNumber = "010-4556-7567"
        )
        val updateMember = memberRepository.findByIdOrNull(
            member.getEmail()
        )
        Assertions.assertEquals(updateMember?.getPassword(), "14564")
    }

    //멤버 삭제
    @Test
    @Transactional
    fun removeMember() {
        testMember?.let { memberRepository.delete(it) }
        Assertions.assertTrue(memberRepository.findById(testMember?.getEmail()!!).isEmpty)
    }

}