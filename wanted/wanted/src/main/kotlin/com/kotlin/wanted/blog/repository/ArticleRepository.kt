package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.Article
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

@RepositoryRestResource
interface ArticleRepository : JpaRepository<Article,Long> {
}