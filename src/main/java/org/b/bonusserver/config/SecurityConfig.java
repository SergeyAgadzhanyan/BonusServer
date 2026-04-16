package org.b.bonusserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(basic -> {
                })
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.GET, "/api/v1/bonuses/*/balance", "/api/v1/bonuses/*/operations")
                        .hasAnyRole("READ", "WRITE")
                        .requestMatchers(HttpMethod.POST, "/api/v1/bonuses/accrual", "/api/v1/bonuses/write-off",
                                "/api/v1/bonuses/reversal")
                        .hasRole("WRITE")
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            @Value("${app.security.read.username}") String readUsername,
            @Value("${app.security.read.password}") String readPassword,
            @Value("${app.security.write.username}") String writeUsername,
            @Value("${app.security.write.password}") String writePassword,
            PasswordEncoder passwordEncoder
    ) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.builder()
                .username(readUsername)
                .password(passwordEncoder.encode(readPassword))
                .roles("READ")
                .build());

        manager.createUser(User.builder()
                .username(writeUsername)
                .password(passwordEncoder.encode(writePassword))
                .roles("WRITE", "READ")
                .build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
