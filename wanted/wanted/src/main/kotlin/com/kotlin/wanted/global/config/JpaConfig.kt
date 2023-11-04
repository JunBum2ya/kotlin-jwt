package com.kotlin.wanted.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig {
    @Bean
    fun auditorAware() : AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }
}