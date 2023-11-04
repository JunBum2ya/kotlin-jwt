package com.kotlin.wanted.blog.controller

import com.kotlin.wanted.security.config.SecurityConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@DisplayName("Blog View test")
@SpringBootTest
class BlogControllerTest {

    @Autowired
    private lateinit var mvc: MockMvc

    @DisplayName("[view][get]")
    @Test
    fun given_when_then() {
        mvc.perform(get("/api/articles")).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))).andDo { println() }
    }

}