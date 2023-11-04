package com.kotlin.wanted.blog.entity

import com.kotlin.wanted.global.converter.BooleanToYnConverter
import com.kotlin.wanted.global.entity.AbstractEntityListener
import jakarta.persistence.*
import lombok.ToString
import java.time.LocalDateTime


@ToString
@Entity
@Table(
    name = "tb_article",
    indexes = [Index(columnList = "article_id")
        , Index(columnList = "title")
        , Index(columnList = "hashTag")
        , Index(columnList = "open")
        , Index(columnList = "createdAt")
        ,Index(columnList = "createdBy")
    ]
)
class Article(
    @Column(length = 30, nullable = false) private var title: String,
    @Column(length = 1000, nullable = false) private var content: String,
    private var hashTag: String? = null,
    @Column(length = 1, nullable = false) @Convert(converter = BooleanToYnConverter::class) private var open: Boolean = true
) :
    AbstractEntityListener() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_id")
    private var id: Long? = null

    @ToString.Exclude
    @OrderBy("article_comment_id")
    @OneToMany(mappedBy = "article", cascade = [CascadeType.ALL])
    private val comments : MutableList<ArticleComment> = mutableListOf()

    fun getTitle(): String {
        return this.title
    }

    fun getContent(): String {
        return this.content
    }

    fun isOpen(): Boolean {
        return this.open
    }

    /**
     * update
     */
    fun update(title: String?, content: String?, open: Boolean?) {
        title?.let { this.title = it }
        content?.let { this.content = content }
        this.open = open ?: true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Article

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}