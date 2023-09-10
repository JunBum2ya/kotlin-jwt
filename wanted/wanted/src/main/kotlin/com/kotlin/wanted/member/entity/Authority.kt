package com.kotlin.wanted.member.entity

import com.kotlin.wanted.global.util.DateUtil
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "tb_authority")
data class Authority(
        @Id @Column(name = "authority_name", length = 30) private var name: String,
        @Column(length = 50) private var comment : String?
) {
    val createDate : LocalDateTime = DateUtil.now()
    var updateDate : LocalDateTime = DateUtil.now()
    fun update(comment: String?) {
        this.comment = comment;
        this.updateDate = DateUtil.now()
    }
}