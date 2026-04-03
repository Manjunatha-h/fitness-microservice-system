package com.fitness.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchnage -> exchnage
                        // .pathMatchers("/actuator/*").permitAll()
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    // to accept the request from the frontend we need to make this
    // corsconfiguration setup
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-User-ID"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

}

// USER-LOGIN FLOW WITH REACT-OAUTH2-CODE-PKCE LIBRARY
// Your app starts inside main.jsx, where AuthProvider wraps the whole React
// tree. That is what gives every child component access to AuthContext,
// including logIn, logOut, token, and tokenData.
//
// In App.jsx, this line gets the auth functions from the context:
//
// const { token, tokenData, logIn, logOut, isAuthenticated } =
// useContext(AuthContext);
//
// So the login button is not doing the redirect itself. It just calls logIn()
// from the library.
//
// When you click the LOGIN button, logIn() triggers the OAuth2 Authorization
// Code with PKCE flow using the URLs in authConfig.js. In your case, that means
// it sends the browser to Keycloak’s authorization endpoint on localhost:8080.
//
// You log in on Keycloak. After success, Keycloak sends the browser back to
// your redirectUri, which is http://localhost:5173 in authConfig.js.
//
// When the browser comes back to your frontend, react-oauth2-code-pkce reads
// the returned authorization code from the URL, exchanges that code for tokens
// at the token endpoint, and then stores the result inside AuthContext.
//
// Your app then sees token and tokenData from the context. That is why this
// effect in App.jsx works:
//
// dispatch(setCredentials({token, user: tokenData}));
//
// That copies the token and user info into Redux and localStorage.
//
// So yes, your understanding is correct: the library is doing the heavy
// lifting. Your component only triggers the login and then consumes the token
// after the library finishes the redirect + exchange flow.
//
// If you want, I can next explain the PKCE part specifically, in plain
// language.
