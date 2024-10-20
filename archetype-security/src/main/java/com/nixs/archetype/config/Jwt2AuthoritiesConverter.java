package com.nixs.archetype.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class Jwt2AuthoritiesConverter
    implements Converter<Jwt, Collection<? extends GrantedAuthority>> {

    private static final String REALM_ACCESS_KEY = "realm_access";
    private static final String RESOURCE_ACCESS_KEY = "resource_access";
    private static final String ROLES_KEY = "roles";

    @Override
    @SuppressWarnings("unchecked")
    public Collection<? extends GrantedAuthority> convert(Jwt jwt) {
        var jwtClaims = jwt.getClaims();

        var realmAccess = (Map<String, Collection<String>>)
            jwtClaims.getOrDefault(REALM_ACCESS_KEY, Map.of());
        var realmRolesStream = realmAccess.getOrDefault(ROLES_KEY, List.of()).stream();

        var resourceAccess = (Map<String, Map<String, Collection<String>>>)
            jwtClaims.getOrDefault(RESOURCE_ACCESS_KEY, Map.of());
        Stream<String> clientRolesStream = resourceAccess.values().stream()
            .map(a -> a.getOrDefault(ROLES_KEY, List.of()))
            .flatMap(Collection::stream);

        return Stream.concat(realmRolesStream, clientRolesStream)
            .distinct()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
