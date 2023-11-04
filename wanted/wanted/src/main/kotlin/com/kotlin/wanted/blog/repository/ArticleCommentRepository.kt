package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.ArticleComment
import com.kotlin.wanted.blog.entity.QArticleComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface ArticleCommentRepository : JpaRepository<ArticleComment, Long>
    , QuerydslPredicateExecutor<ArticleComment> {
}