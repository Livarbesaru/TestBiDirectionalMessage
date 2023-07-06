package com.message.bidirectional.util;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Slf4j
@Component
public class JwtCustomAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken>{
    private final String ACCESS_MAP_WITH_COLLECTION_OF_ROLES = "realm_access";
    private final String ACCESS_ROLES_LIST = "roles";
    private final String ROLE_PREFIX = "ROLE_";
    private final String PRINCIPAL_USERNAME = "preferred_username";

    @Override
    public JwtAuthenticationToken convert(Jwt jwt) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String name = jwt.getClaim(PRINCIPAL_USERNAME);
        LinkedTreeMap<String, Collection<String>> map = jwt.getClaim(ACCESS_MAP_WITH_COLLECTION_OF_ROLES);
        if (!ObjectUtils.isEmpty(map) && !ObjectUtils.isEmpty(map.get(ACCESS_ROLES_LIST))) {
            authorities = map.get(ACCESS_ROLES_LIST)
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                    .toList();
        }
        name = name != null ? name : " ";
        return new JwtAuthenticationToken(jwt, authorities, name);
    }
}
