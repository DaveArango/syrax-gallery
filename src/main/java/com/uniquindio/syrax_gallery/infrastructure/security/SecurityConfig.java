package com.uniquindio.syrax_gallery.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final RespuestasSeguridad respuestasSeguridad;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Infraestructura
                        .requestMatchers(
                                "/api/auth/login",
                                "/error",
                                "/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/h2-console/**"
                        ).permitAll()

                        // Público
                        .requestMatchers(HttpMethod.GET,
                                "/api/azantys",               // buscar Azantys
                                "/api/azantys/mas-solicitados",
                                "/api/azantys/*",             // perfil público
                                "/api/caraxes",               // catálogo (solo obras publicadas)
                                "/api/sunfyre"                // catálogo (solo servicios publicados)
                        ).permitAll()

                        // Registro público de Azantys y Zentys (crea también su cuenta de login)
                        .requestMatchers(HttpMethod.POST, "/api/azantys", "/api/zentys").permitAll()

                        // Solo el Jovennio (ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/azantys/*/revision").hasAuthority("ADMIN")

                        // Solo el Azantys (vendedor)
                        .requestMatchers(HttpMethod.POST, "/api/sunfyre").hasAuthority("AZANTYS")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/sunfyre/*/publicar",
                                "/api/sunfyre/*/iniciar",
                                "/api/sunfyre/*/finalizar",
                                "/api/sunfyre/*/reprogramar",
                                "/api/azantys/*/identidad"
                        ).hasAuthority("AZANTYS")
                        .requestMatchers(HttpMethod.GET, "/api/sunfyre/mis-servicios").hasAuthority("AZANTYS")

                        // Solo el Zentys (comprador)
                        .requestMatchers(HttpMethod.POST, "/api/kelitis").hasAuthority("ZENTYS")
                        .requestMatchers(HttpMethod.PUT, "/api/kelitis/*/aprobar").hasAuthority("ZENTYS")

                        // Todo lo demás exige token
                        .anyRequest().authenticated()
                )
                // 401 y 403 con JSON claro (mismo formato que GlobalExceptionHandler)
                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint(respuestasSeguridad)
                        .accessDeniedHandler(respuestasSeguridad))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(respuestasSeguridad)
                        .accessDeniedHandler(respuestasSeguridad)
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
