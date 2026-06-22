package io.github.tewyss.developer_dashboard.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationEventListener {

    private final LoginAttemptService loginAttemptService;

    public AuthenticationEventListener(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        loginAttemptService.loginSucceeded();
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        loginAttemptService.loginFailed();
    }
}
