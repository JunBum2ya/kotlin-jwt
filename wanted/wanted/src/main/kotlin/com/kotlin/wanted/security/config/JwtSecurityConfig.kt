package com.kotlin.wanted.security.config

import com.kotlin.wanted.security.component.TokenProvider
import com.kotlin.wanted.security.filter.CustomJwtFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtSecurityConfig(private val tokenProvider: TokenProvider) :
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        // security 로직에 JwtFilter 등록
        http.addFilterBefore(CustomJwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
    }
}

