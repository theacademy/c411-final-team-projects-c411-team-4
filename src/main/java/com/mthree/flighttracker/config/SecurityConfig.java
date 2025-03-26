package com.mthree.flighttracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api").permitAll() // Keep most pages public
                        .requestMatchers("/user/**").authenticated() // Require login for user features
                        .anyRequest().permitAll()
                )
                .formLogin(withDefaults()) // Enable form login
                .logout(logout -> logout.logoutSuccessUrl("/").permitAll()); // Logout redirects to home

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER") // Only standard user role
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}