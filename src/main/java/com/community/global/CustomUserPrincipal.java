package com.community.global;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record CustomUserPrincipal(
        Long memberId,
        Collection<? extends GrantedAuthority> authorities
) {
}
