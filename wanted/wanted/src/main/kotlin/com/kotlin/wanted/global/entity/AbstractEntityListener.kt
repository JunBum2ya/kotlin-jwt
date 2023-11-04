package com.kotlin.wanted.global.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractEntityListener {
    @CreatedDate
    private var createdAt: LocalDateTime? = null

    @CreatedBy
    private var createdBy: String? = null

    @LastModifiedDate
    private var modifiedAt: LocalDateTime? = null

    @LastModifiedBy
    private var modifiedBy: String? = null

    fun getCreateBy() : String? {
        return this.createdBy
    }

    fun getCreateAt() : LocalDateTime? {
        return this.createdAt
    }

    fun getModifiedBy() : String? {
        return this.modifiedBy
    }

    fun getModifiedAt() : LocalDateTime? {
        return this.modifiedAt
    }
}