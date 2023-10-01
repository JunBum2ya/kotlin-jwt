package com.kotlin.wanted.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "tb_token")
class RefreshToken(
    private var token: String
) {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private var id: Long? = null
    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL]) @JoinColumn(name = "email") private var member: Member? = null
    fun getToken() : String {
        return this.token
    }

    fun update(token : String) {
        this.token = token
    }

}