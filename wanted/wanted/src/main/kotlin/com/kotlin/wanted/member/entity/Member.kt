package com.kotlin.wanted.member.entity

import com.kotlin.wanted.global.converter.BooleanToYnConverter
import com.kotlin.wanted.global.util.DateUtil
import com.kotlin.wanted.member.code.Gender
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tb_member")
class Member(
    @Id private val email: String,
    private var password: String,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tb_user_authority",
        joinColumns = [JoinColumn(name = "email")],
        inverseJoinColumns = [JoinColumn(name = "authority_name")]
    )
    private val authorities: MutableList<Authority>,
    @Enumerated(value = EnumType.STRING) private var gender: Gender?,
    private var age: Int?,
    @Column(length = 20) private var phoneNumber: String?
) {
    private val createDate: LocalDateTime = DateUtil.now()
    private var updateDate: LocalDateTime = DateUtil.now()

    @Column(name = "active_yn", length = 1)
    @Convert(converter = BooleanToYnConverter::class)
    var isActive: Boolean = true

    //update 실행
    fun update(
        password: String,
        authorityList: MutableList<Authority>?,
        gender: Gender?,
        age: Int?,
        phoneNumber: String?
    ) {
        this.password = password
        this.gender = gender
        this.age = age
        this.phoneNumber = phoneNumber
        this.authorities.clear()
        authorityList.let {
            this.authorities.clear()
            if (it != null) {
                this.authorities.addAll(it)
            }
        }
        updateDate = DateUtil.now()
    }

    //getter
    fun getEmail(): String {
        return this.email
    }

    fun getPassword(): String {
        return this.password
    }

    fun getAuthorities(): MutableList<Authority> {
        return this.authorities
    }

    fun getGender(): Gender? {
        return this.gender
    }

    fun getAge(): Int? {
        return this.age
    }

    fun getPhoneNumber(): String? {
        return this.phoneNumber
    }
}