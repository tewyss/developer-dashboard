package io.github.tewyss.developer_dashboard.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;


@Configuration
public class SecurityConfig {

    private final boolean h2ConsoleEnabled;

    public SecurityConfig(@Value("${spring.h2.console.enabled:false}") boolean h2ConsoleEnabled) {
        this.h2ConsoleEnabled = h2ConsoleEnabled;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll();
                    auth.requestMatchers("/error").permitAll();
                    auth.requestMatchers("/actuator/health").permitAll();
                    auth.requestMatchers("/", "/about", "/contact", "/projects/**", "/blog/**", "/login")
                            .permitAll();
                    // H2 web console — only reachable when explicitly enabled (dev).
                    if (h2ConsoleEnabled) {
                        auth.requestMatchers("/h2-console/**").permitAll();
                    }
                    auth.requestMatchers("/admin/**").authenticated();
                    auth.anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/admin", true)
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout")
                        .permitAll()
                );

        if (h2ConsoleEnabled) {
            http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
            http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        }

        return http.build();
    }


    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String target = (exception instanceof LockedException) ? "/login?locked" : "/login?error";
            response.sendRedirect(request.getContextPath() + target);
        };
    }
}
