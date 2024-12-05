package dev.littlemad.oauth2_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    /**
     * Creates a {@link JwtAuthenticationConverter} bean to extract roles from the
     * {@code realm_access.roles} claim in the JWT token, as configured in the Keycloak server.
     *
     * <p>This converter is automatically picked up by Spring Boot and ensures that roles
     * defined in the {@code realm_access.roles} claim are properly mapped to
     * Spring Security authorities.</p>
     *
     * @return a configured {@link JwtAuthenticationConverter} instance for mapping JWT roles.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(source -> mapAuthorities(source.getClaims()));
        return converter;
    }

    private List<GrantedAuthority> mapAuthorities(final Map<String, Object> attributes) {

        final Map<String, Object> realmAccess =
                ((Map<String, Object>) attributes.getOrDefault("realm_access", Collections.emptyMap()));

        final Collection<String> roles =
                ((Collection<String>) realmAccess.getOrDefault("roles", Collections.emptyList()));

        return roles.stream()
                .map(role -> ((GrantedAuthority) new SimpleGrantedAuthority(role)))
                .toList();
    }


    /**
     * Configures the security filter chain for the application as a resource server
     * using OAuth2 with JWT authentication. The method disables CSRF protection,
     * allows Cross-Origin Resource Sharing (CORS) with default settings, and permits
     * all requests without requiring authentication.
     *
     * <p>This setup is suitable for applications that act as resource servers
     * and validate incoming JWT tokens to secure API endpoints.</p>
     *
     * @param http the {@link HttpSecurity} object used to configure the security filters.
     * @return a {@link SecurityFilterChain} instance configured for JWT-based OAuth2 resource server security.
     * @throws Exception if an error occurs while building the security configuration.
     */
    @Bean
    public SecurityFilterChain oauth2SecurityFilterChain(final HttpSecurity http) throws Exception {
        return http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }

    // CSRF can be completely deactivated if the application only has a REST API
    // If there are server-side forms, CSRF should still be active for these parts of the application.

    // With authorize.anyRequest().permitAll(), the entire application is accessible without a token
    // However, with the @EnableMethodSecurity we enable annotations to shield the RestController to be
    // protected directly.

    // CREDITS:
    // https://bootify.io/spring-security/spring-boot-resource-server-for-keycloak.html
}
