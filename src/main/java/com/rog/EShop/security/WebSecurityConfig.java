package com.rog.EShop.security;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/token", "/api/users/register").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );


        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();

        return new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {
                Collection<GrantedAuthority> grantedAuthorities = delegate.convert(jwt);

                if (jwt.getClaim("realm_access") == null) {
                    return grantedAuthorities;
                }
                JSONObject realmAccess = jwt.getClaim("realm_access");
                if (realmAccess.get("roles") == null) {
                    return grantedAuthorities;
                }
                JSONArray roles = null;
                roles = (JSONArray) realmAccess.get("roles");

                final List<SimpleGrantedAuthority> keycloakAuthorities = roles
                        .stream()
                        .map(role -> new SimpleGrantedAuthority("" + role)).toList();
                grantedAuthorities.addAll(keycloakAuthorities);

                return grantedAuthorities;
            }
        };
    }
}
