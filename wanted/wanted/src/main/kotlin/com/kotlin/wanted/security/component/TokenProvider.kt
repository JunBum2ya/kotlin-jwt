package com.kotlin.wanted.security.component

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Slf4j
@Component
class TokenProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.token-validity-in-seconds}") tokenValidityInSeconds: Long
) : InitializingBean {

    private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)

    protected val AUTHORITIES_KEY = "auth"
    protected val tokenValidityInMilliseconds: Long = tokenValidityInSeconds * 1000
    protected var key: Key? = null
    protected val secret: String = secret
    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(this.secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createToken(authentication: Authentication): String {
        val authorities = authentication.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))
        val now = Date().time
        val validity = Date(now + tokenValidityInMilliseconds)
        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(this.AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity)
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val authorities = Arrays.stream<String>(
            claims[this.AUTHORITIES_KEY].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
            .map<SimpleGrantedAuthority> { role: String ->
                SimpleGrantedAuthority(
                    role
                )
            }
            .toList()
        val user = null
        return UsernamePasswordAuthenticationToken(user, token, authorities)
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("만료된 JWT 토큰입니다.")
        }
        return false
    }

}