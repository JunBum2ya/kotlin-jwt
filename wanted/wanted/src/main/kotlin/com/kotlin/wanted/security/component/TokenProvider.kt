package com.kotlin.wanted.security.component

import com.kotlin.wanted.member.dto.CustomUserDetails
import com.kotlin.wanted.member.entity.Authority
import com.kotlin.wanted.member.entity.Member
import com.kotlin.wanted.security.dto.JwtToken
import com.kotlin.wanted.security.dto.TokenSaveParameter
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
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

@Component
class TokenProvider(
    @Value("\${jwt.secret}") secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") accessTokenValidityInSeconds: Long,
    @Value("\${jwt.refresh-token-validity-in-seconds}") refreshTokenValidityInSeconds: Long
) : InitializingBean {

    private val logger: Logger = LoggerFactory.getLogger(TokenProvider::class.java)

    protected val AUTHORITIES_KEY = "auth"
    protected val accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000
    protected val refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000
    protected var key: Key? = null
    protected val secret: String = secret
    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(this.secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createAccessToken(authentication: Authentication): String {
        val authorities = authentication.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.toList())
        return createRefreshToken(TokenSaveParameter(email = authentication.name, authorities = authorities))
    }

    fun createToken(parameter: TokenSaveParameter): JwtToken {
        val refreshToken = createRefreshToken(parameter)
        val accessToken = createAccessToken(parameter)
        return JwtToken(username = parameter.email, refreshToken = refreshToken, accessToken = accessToken)
    }

    fun createRefreshToken(authentication: Authentication): String {
        val authorities = authentication.authorities
            .stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.toList())
        return createRefreshToken(TokenSaveParameter(email = authentication.name, authorities = authorities))
    }

    fun createAccessToken(parameter: TokenSaveParameter): String {
        val authorities = parameter.authorities
            .stream()
            .collect(Collectors.joining(","))
        return Jwts.builder()
            .setSubject(parameter.email)
            .claim(this.AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(Date(Date().time + accessTokenValidityInMilliseconds))
            .compact()
    }

    fun createRefreshToken(parameter: TokenSaveParameter): String {
        val authorities = parameter.authorities
            .stream()
            .collect(Collectors.joining(","))
        return Jwts.builder()
            .setSubject(parameter.email)
            .claim(this.AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(Date(Date().time + refreshTokenValidityInMilliseconds))
            .compact()
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val authorities = Arrays.stream(
            claims[this.AUTHORITIES_KEY].toString().split(",".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
            .map { role: String -> SimpleGrantedAuthority(role) }
            .toList()
        val user =
            CustomUserDetails(username = claims.subject, password = "", authorities = authorities, isActive = true)
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
        } catch (e : SignatureException) {
            logger.info("잘못된 JWT 토큰입니다.")
        }
        return false
    }

}