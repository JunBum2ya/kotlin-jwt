package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.QArticle
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer
import org.springframework.data.querydsl.binding.QuerydslBindings

interface ArticleRepositoryCustom : QuerydslBinderCustomizer<QArticle>