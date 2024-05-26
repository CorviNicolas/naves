package com.w2m.naves.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.stream.Stream;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           @Value("${spring.h2.console.enabled:false}") boolean h2Console) throws Exception {
        return http
                .authorizeHttpRequests(authz -> {
                            authz.requestMatchers(multipleAntPathRequestMatchers(HttpMethod.GET, "/docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")).permitAll();
                            if (h2Console) {
                                authz.requestMatchers(PathRequest.toH2Console()).permitAll();
                                authz.requestMatchers("/h2-console/**");
                            }
                            authz.anyRequest().authenticated();
                        }
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("user"))
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder(@Value("${password.strength:10}") int passwordStrength) {
        return new BCryptPasswordEncoder(passwordStrength);
    }

    private static RequestMatcher[] multipleAntPathRequestMatchers(HttpMethod method, String... patterns) {
        return Stream.of(patterns).map(pattern -> (RequestMatcher) AntPathRequestMatcher.antMatcher(method, pattern))
                .toArray(RequestMatcher[]::new);
    }
}
