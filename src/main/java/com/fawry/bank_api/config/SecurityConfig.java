package com.fawry.bank_api.config;

import com.fawry.bank_api.exception.EntityNotFoundException;
import com.fawry.bank_api.repository.UserRepository;
import com.fawry.bank_api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {


    private final UserRepository userRepository;
    public SecurityConfig(UserRepository userRepository)
    {
        this.userRepository=userRepository;
    }

    private static final String[] AUTH_WHITELIST = {
            "api/auth/**"
    };
    private static final String[] AUTH_ADMIN = {
            "/api/user/activate/**",
            "/api/user/deactivate/**",
            "/api/user" // Endpoint to get all users
    };
    private static final String[] AUTH_USER = {
            "/api/user/{userId}", // Get profile
            "/api/user/change-password",
            "/api/user/reset-password"
    };

    @Bean
    JwtAuthenticationFilter authenticationJwtTokenFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(AUTH_ADMIN).hasRole("ADMIN")  // Only admins can access admin routes
                        .requestMatchers(AUTH_USER).hasAnyRole("ADMIN","USER")  // Client access
                        .anyRequest().authenticated()
                );

        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("user with this email doesn't exist"));
    }
}
