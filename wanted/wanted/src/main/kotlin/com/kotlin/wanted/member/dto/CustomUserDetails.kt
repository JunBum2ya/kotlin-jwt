package com.kotlin.wanted.member.dto

import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.util.StringUtils
import java.io.Serializable
import java.util.stream.Collectors

class CustomUserDetails(
    private val username: String,
    private val password: String,
    val authorities: List<GrantedAuthority>,
    val isActive: Boolean
) : UserDetails {
    companion object {
        fun from(member: Member): UserDetails {
            return CustomUserDetails(username = member.getEmail(),
                password = member.getPassword(),
                authorities = member.getAuthorities()
                    .stream()
                    .map(Authority::getName)
                    .map { role: String -> SimpleGrantedAuthority(role) }
                    .collect(Collectors.toList()), isActive = member.isActive
            )
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return this.isActive
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return this.isActive
    }

    override fun toString(): String {
        return "{ \"username\" : \"${this.username}\", \"password\" : \"${this.password}\" }"
    }
}