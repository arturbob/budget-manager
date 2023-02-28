package com.example.budgetmanager.config.security;

import com.example.budgetmanager.config.security.jwt.CustomAuthenticationEntryPoint;
import com.example.budgetmanager.config.security.jwt.JwtAuthenticationFilter;
import com.example.budgetmanager.repository.CustomerRepository;
import com.example.budgetmanager.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final CustomerRepository customerRepository;
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint entryPoint;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(customerRepository);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests((authz) -> {
                            try {
                                authz
                                        .antMatchers(HttpMethod.POST, "/api/v1/figures/**").hasAnyAuthority("ADMIN", "CREATOR")
                                        .antMatchers(HttpMethod.GET, "/api/v1/figures/**").hasAnyAuthority("CREATOR", "USER", "ADMIN")
                                        .antMatchers(HttpMethod.POST, "/api/v1/customers").permitAll()
                                        .antMatchers(HttpMethod.POST, "/api/v1/auth").permitAll()
                                        .anyRequest()
                                        .authenticated()
                                        .and()
                                        .sessionManagement()
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                        .and()
                                        .authenticationProvider(authenticationProvider())
                                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                                        .exceptionHandling()
                                        .authenticationEntryPoint(entryPoint)
                                        .accessDeniedHandler(accessDeniedHandler());
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
