package com.muhammadtello.blog

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableHypermediaSupport
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.core.GrantedAuthorityDefaults
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Configuration
@EnableWebSecurity
class BlogConfiguration: WebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
    override fun userDetailsService(): UserDetailsService {
        return Users()
    }
    override fun configure(http: HttpSecurity?) {
        http!!
                .authorizeRequests().antMatchers("/api/article", "/article/*", "/")
                .permitAll()
                .and()
                .formLogin().loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().fullyAuthenticated()
                .and().httpBasic()
                .and().csrf().disable()
    }
}

@Service class Users: UserDetailsService {
    @Autowired
    private lateinit var repo: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = repo.findByLogin(username!!) ?: throw UsernameNotFoundException(username)
        val auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN")
        return User(user.login, user.password, auth)
    }
}