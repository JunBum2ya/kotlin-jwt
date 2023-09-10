package com.kotlin.wanted.member.entity

import com.kotlin.wanted.global.util.DateUtil
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_member")
class Member(
        @Id private val email: String,
        private var password: String,
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "tb_user_authority"
                , joinColumns = [JoinColumn(name = "email")]
                , inverseJoinColumns = [JoinColumn(name = "authority_name")])
        private val authorities : MutableList<Authority>
) {
    private val createDate : LocalDateTime = DateUtil.now()
    private var updateDate : LocalDateTime = DateUtil.now()
    
    //update 실행
    fun update(password: String, authorityList: MutableList<Authority>?) {
        this.password = password
        updateDate = DateUtil.now()
    }
    //getter
    fun getEmail() : String {
        return this.email
    }

    fun getPassword() : String {
        return this.password
    }

    fun getAuthorities() : MutableList<Authority> {
        return this.authorities;
    }
}