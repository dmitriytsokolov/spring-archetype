package com.nixs.archetype.config;

import static org.springframework.http.HttpHeaders.WWW_AUTHENTICATE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.web.cors.CorsConfiguration.ALL;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ChannelSecurityConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    @Value("${spring.security.public-endpoints}")
    private String[] publicEndpoints;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
        Jwt2AuthenticationConverter authenticationConverter,
        ServerProperties serverProperties) throws Exception {
        return http
            .oauth2ResourceServer(configurer -> customize(configurer, authenticationConverter))
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(this::customize)
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(this::customize)
            .requiresChannel(registry -> customize(registry, serverProperties))
            .authorizeHttpRequests(this::customize)
            .build();
    }

    private void customize(OAuth2ResourceServerConfigurer<HttpSecurity> configurer,
        Jwt2AuthenticationConverter authenticationConverter) {
        configurer.jwt(jwtConfigurer -> jwtConfigurer
            .jwtAuthenticationConverter(authenticationConverter));
    }

    private void customize(SessionManagementConfigurer<HttpSecurity> configurer) {
        configurer.sessionCreationPolicy(STATELESS);
    }

    private void customize(ExceptionHandlingConfigurer<HttpSecurity> configurer) {
        configurer.authenticationEntryPoint((request, response, authException) -> {
            response.addHeader(WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
            response.sendError(UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase());
        });
    }

    private void customize(
        ChannelSecurityConfigurer<HttpSecurity>.ChannelRequestMatcherRegistry registry,
        ServerProperties serverProperties) {
        if (serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled()) {
            registry.anyRequest().requiresSecure();
        } else {
            registry.anyRequest().requiresInsecure();
        }
    }

    private void customize(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(publicEndpoints)
            .permitAll()
            .anyRequest()
            .authenticated();
    }

    //permissive cors configuration, update for your needs
    private CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(ALL));
        configuration.setAllowedMethods(List.of(ALL));
        configuration.setAllowedHeaders(List.of(ALL));
        configuration.setExposedHeaders(List.of(ALL));
        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}