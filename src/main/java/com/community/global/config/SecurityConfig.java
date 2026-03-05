package com.community.global.config;

import com.community.global.jwt.JWTProvider;
import com.community.global.jwt.JwtAuthenticationFilter;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTProvider jwtProvider;
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtProvider, memberRepositoryAdapter);
    }

    /**
     * [공개 체인 - Order 1]
     * auth / swagger 경로는 JWT 필터 자체를 등록하지 않는다.
     * shouldNotFilter 와 달리 필터 인스턴스가 아예 체인에 없으므로
     * 만료 토큰이 붙어 있어도 절대 401이 발생하지 않는다.
     *
     * ※ 현재 JwtAuthenticationFilter 의 shouldNotFilter() 로도 동일 효과를 낼 수 있다.
     *   이 듀얼 체인 방식은 "필터 적용 범위를 SecurityConfig 에서 명시적으로 관리"하고 싶을 때 선택한다.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/api/v1/auth/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-resources/**"
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        // JWT 필터 미등록 — 토큰 유무·만료 여부와 무관하게 항상 통과
        return http.build();
    }

    /**
     * [인증 필요 체인 - Order 2]
     * 나머지 모든 경로. JWT 필터가 실제로 동작한다.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain protectedFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/v1/posts").permitAll()
                        .requestMatchers("/api/v1/categories").permitAll()
                        .requestMatchers("/api/v1/notifications/**").permitAll()
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/chatbot/**").permitAll()
//                        .requestMatchers("/api/v1/comments/**").permitAll()
                        .requestMatchers("/api/v1/workout-logs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
