
package org.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security configuration class responsible for setting up security filters,
 * CSRF protection, CORS policies, endpoint access rules, and authentication entry points.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Configures the security filter chain for incoming HTTP requests.
   * Disables CSRF, enables CORS, defines public and protected endpoints,
   * configures unauthorized entry points, and registers the custom JWT authentication filter.
   *
   * @param httpSecurity The HttpSecurity object to configure web-based security.
   * @return The configured SecurityFilterChain instance.
   * @throws Exception If an error occurs during security configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .csrf(csrf -> csrf.disable())
        .cors(cors -> {})
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/api/auth/login-employee",
                "/api/auth/register-employee"
            )
            .permitAll()
            .anyRequest()
            .authenticated()
        )
        .exceptionHandling(exceptions -> exceptions
            .authenticationEntryPoint(
                new HttpStatusEntryPoint(
                    HttpStatus.UNAUTHORIZED
                )
            )
        )
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        );

    return httpSecurity.build();
  }
}
