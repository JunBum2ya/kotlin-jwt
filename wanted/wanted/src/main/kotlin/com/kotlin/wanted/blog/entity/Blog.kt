package com.kotlin.wanted.blog.entity

import com.kotlin.wanted.global.converter.BooleanToYnConverter
import com.kotlin.wanted.global.entity.AbstractEntityListener
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "tb_blog")
class Blog(
    @Column(length = 1, nullable = false) @Convert(converter = BooleanToYnConverter::class) var open: Boolean = true,
    @Column(length = 30, nullable = false) var name : String,
) : AbstractEntityListener() {
    @Id
    private var id: Long? = null
}