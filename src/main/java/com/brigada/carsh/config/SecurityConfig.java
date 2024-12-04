package com.brigada.carsh.config;

import com.brigada.carsh.security.jwt.JwtAuthenticationFilter;
import com.brigada.carsh.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableScheduling
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .authorizeHttpRequests(request -> request
//                                .requestMatchers("/api/auth/**","/swagger-ui/**", // отсюда
//                                        "/v3/api-docs/**", "/swagger-ui.html").permitAll()
//                                .requestMatchers("/api/accident-reports/**").hasAnyRole("ADMIN", "ACCIDENT_COMMISSAR")
//                                .requestMatchers("/api/bookings/**").authenticated()
//                                .requestMatchers(HttpMethod.GET, "/api/cars/**").authenticated()
//                                .requestMatchers("/api/cars/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET, "/api/document-verification/**").authenticated()
//                                .requestMatchers("/api/document-verification/**").hasRole("ADMIN")
//                                .requestMatchers("/api/document-verification/**").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.POST, "/api/document-verification/**").authenticated()
//                                .requestMatchers("/api/document-verification/**").hasRole("ADMIN")
//                                .requestMatchers("/api/fines/my").authenticated()
//                                .requestMatchers(HttpMethod.PUT, "/api/fines/**").authenticated()
//                                .requestMatchers("/api/fines/**").hasAnyRole("ADMIN", "ACCIDENT_COMMISSAR")
//                                .requestMatchers("/api/maintenance/**").hasRole("ADMIN")
//                                .requestMatchers("/api/payments/my").authenticated()
//                                .requestMatchers(HttpMethod.POST, "/api/payments/**").authenticated()
//                                .requestMatchers("/api/payments/**").hasAnyRole("ADMIN", "ACCIDENT_COMMISSAR")
//                                .requestMatchers(HttpMethod.GET, "/api/support-tickets/my", "/api/support-tickets/{\\d+}").authenticated()
//                                .requestMatchers("/api/support-tickets/**").hasAnyRole("ADMIN", "ACCIDENT_COMMISSAR")
//                                .anyRequest().authenticated() // и до сюда закомментить
                                .anyRequest().permitAll() // а это раскомментить
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
