package com.kotlin.wanted.security.component

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CustomPasswordEncoder : PasswordEncoder {

    private val bCryptPasswordEncoder: BCryptPasswordEncoder = BCryptPasswordEncoder()
    override fun encode(rawPassword: CharSequence): String {
        return bCryptPasswordEncoder.encode(rawPassword)
    }

    override fun matches(rawPassword: CharSequence, encodedPassword: String): Boolean {
        return bCryptPasswordEncoder.matches(rawPassword, encodedPassword)
    }
}