package com.kotlin.wanted.security.config

import com.kotlin.wanted.global.handler.JwtAccessDeniedHandler
import com.kotlin.wanted.security.component.JwtAuthenticationEntryPoint
import com.kotlin.wanted.security.component.TokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class SecurityConfig(
    val tokenProvider: TokenProvider,
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { o -> o.disable() }
            .exceptionHandling { o ->
                o.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .headers { o -> o.frameOptions { obj -> obj.sameOrigin() } }
            .sessionManagement { o -> o.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { o ->
                o.requestMatchers("/member/login", "/member/join", "/member/issue-token").permitAll().anyRequest()
                    .authenticated()
            }
            .apply(JwtSecurityConfig(tokenProvider))
        return httpSecurity.build()
    }

}

