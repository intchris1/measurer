package com.offsidegaming.measurer.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class SecurityUtils {

    public static final String USER_ROLE = "ROLE_USER";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    public static UserData getCurrentUserData() {
        Optional<Authentication> auth = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
        Set<String> roles = auth.map(Authentication::getAuthorities)
                .filter(ObjectUtils::isNotEmpty)
                .map(authorities -> authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
        String username = auth.map(Authentication::getPrincipal).filter(User.class::isInstance)
                .map(it -> ((User) it).getUsername()).orElse(null);
        return new UserData(username, roles);
    }

    @RequiredArgsConstructor
    @Getter
    public static class UserData {

        @Nullable
        private final String username;

        @NonNull
        private Collection<String> roles;

    }
}
