package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.Article
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private lateinit var articleRepository: ArticleRepository

    @DisplayName("게시글 생성")
    @Test
    fun saveArticle() {
        val article = Article(title = "test article", content = "첫번째 글을 작성합니다.")
        val savedArticle = articleRepository.save(article)
        Assertions.assertEquals(savedArticle,article)
        Assertions.assertEquals(savedArticle.getTitle(),article.getTitle())
        Assertions.assertEquals(savedArticle.getContent(),article.getContent())
        Assertions.assertNotNull(savedArticle.getCreateAt())
        Assertions.assertNotNull(savedArticle.getCreateBy())
        Assertions.assertNotNull(savedArticle.getModifiedAt())
        Assertions.assertNotNull(savedArticle.getModifiedBy())
    }

}