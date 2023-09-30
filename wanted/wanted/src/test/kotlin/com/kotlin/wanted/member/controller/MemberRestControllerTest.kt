package com.kotlin.wanted.member.controller

import com.kotlin.wanted.WantedApplication
import com.kotlin.wanted.member.dto.CustomUserDetails
import com.kotlin.wanted.member.dto.MemberLoginRequest
import com.kotlin.wanted.member.service.MemberService
import com.kotlin.wanted.security.component.TokenProvider
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.context.support.WithSecurityContext
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils

@SpringBootTest
class MemberRestControllerTest {

    @Autowired
    private lateinit var memberRestController: MemberRestController

    @Autowired
    private lateinit var memberService: MemberService

    var mock : MockMvc? = null

    @BeforeEach
    fun initMock() {
        mock = MockMvcBuilders.standaloneSetup(memberRestController).build()
    }

    @Test
    fun testJoin() {
        mock?.let {
            val composerByName = "email"
            val composerByResult = "result"
            val builder = post("/member/join").contentType("application/json").content("{\n" +
                    "    \"email\" : \"tistory2@naver.com\",\n" +
                    "    \"password\" : \"123456789\"\n" +
                    "}")
            val result = it.perform(builder).andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(composerByName).value("tistory2@naver.com"))
                .andExpect(jsonPath(composerByResult).value(true))
                .andReturn()
            println(result.response.contentAsString)
        }?:AssertionError()
    }

    @Test
    fun testLogin() {
        mock?.let {
            val composerByName = "email"
            val composerByResult = "result"
            val composerByToken = "token"
            val builder = post("/member/login").contentType("application/json").content("{\n" +
                    "    \"email\" : \"tistory@tistory.com\",\n" +
                    "    \"password\" : \"123456789\"\n" +
                    "}")
            val result = it.perform(builder).andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath(composerByName).value("tistory@tistory.com"))
                .andExpect(jsonPath(composerByResult).value(true))
                .andExpect(jsonPath(composerByToken).exists())
                .andReturn()
            println(result.response.contentAsString)
        }?:AssertionError()
    }

    @Test
    fun testAuthenticate() {
        mock?.let {
            val builder = get("/member/test")
            val result = it.perform(builder).andExpect(status().isOk)
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn()
            println(result.response.contentAsString)
        }?:AssertionError()
    }

}