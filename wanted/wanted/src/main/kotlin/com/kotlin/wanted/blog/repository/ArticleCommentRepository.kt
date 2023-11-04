package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.ArticleComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface ArticleCommentRepository : JpaRepository<ArticleComment,Long> {
}