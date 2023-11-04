package com.kotlin.wanted.blog.repository

import com.kotlin.wanted.blog.entity.QArticle
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.querydsl.binding.QuerydslBindings
import org.springframework.stereotype.Repository

@Repository
class ArticleRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : ArticleRepositoryCustom {
    override fun customize(bindings: QuerydslBindings, root: QArticle) {
        bindings.excludeUnlistedProperties(false)
        bindings.including(root.title, root.hashTag, root.createdBy, root.createAt, root.open)
        bindings.bind(root.title).first { path,value -> path.containsIgnoreCase(value)}
    }
}