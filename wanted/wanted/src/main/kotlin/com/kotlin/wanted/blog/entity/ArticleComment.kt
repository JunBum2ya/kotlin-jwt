package com.kotlin.wanted.blog.entity

import com.kotlin.wanted.global.entity.AbstractEntityListener
import jakarta.persistence.*
import lombok.ToString

@ToString
@Entity
@Table(
    name = "tb_article_comment",
    indexes = [Index(columnList = "article_comment_id"), Index(columnList = "createdAt"), Index(columnList = "createdBy")]
)
class ArticleComment(
    var content: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "article_id") val article: Article
) :
    AbstractEntityListener() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_comment_id")
    private var id: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArticleComment

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}