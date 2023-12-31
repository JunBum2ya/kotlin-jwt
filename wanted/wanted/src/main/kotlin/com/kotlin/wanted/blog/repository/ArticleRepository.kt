package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.Article
import com.kotlin.wanted.blog.entity.QArticle
import com.querydsl.core.types.dsl.SimpleExpression
import com.querydsl.core.types.dsl.StringExpression
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource

@RepositoryRestResource
interface ArticleRepository : JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, ArticleRepositoryCustom