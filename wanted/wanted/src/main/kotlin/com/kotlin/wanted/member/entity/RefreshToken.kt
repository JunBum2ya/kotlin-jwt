package com.kotlin.wanted.member.entity

import jakarta.persistence.*

@Entity
@Table(name = "tb_token")
class RefreshToken(
    @Column(unique = true) private var email: String,
    private var token: String
) {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) private var id: Long? = null
    fun getEmail() : String {
        return this.email
    }
    fun getToken() : String {
        return this.token
    }

    fun update(token : String) {
        this.token = token
    }

}