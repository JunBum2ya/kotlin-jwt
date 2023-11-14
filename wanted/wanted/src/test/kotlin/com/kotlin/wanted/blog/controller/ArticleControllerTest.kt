package com.kotlin.wanted.blog.controller

import org.assertj.core.api.BDDAssumptions.given
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@DisplayName("View 컨트롤러 - 게시글")
@SpringBootTest
class ArticleControllerTest {

    @Autowired
    private lateinit var articleController: ArticleController
    private var mvc : MockMvc? = null

    @BeforeEach
    fun initMvc() {
        mvc = MockMvcBuilders.standaloneSetup(articleController).build()
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    fun givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() {
        // Given

        //when & then
        mvc?.let {
            val result = it.perform(get("/articles"))
                .andExpect(status().isOk)
                .andReturn()
            println(result.response.contentType)
        }?:throw Exception("not empty mvc")
    }

}