package io.github.tewyss.developer_dashboard.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final HttpServletRequest request;
    private final Map<String, Attempt> attempts = new ConcurrentHashMap<>();

    public LoginAttemptService(HttpServletRequest request) {
        this.request = request;
    }

    public void loginSucceeded() {
        attempts.remove(clientKey());
    }

    public void loginFailed() {
        attempts.compute(clientKey(), (key, current) -> {
            if (current != null && current.lockedUntil() != null
                    && Instant.now().isBefore(current.lockedUntil())) {
                return current;
            }
            int count = (current == null ? 0 : current.count()) + 1;
            Instant lockedUntil = count >= MAX_ATTEMPTS ? Instant.now().plus(LOCK_DURATION) : null;
            return new Attempt(count, lockedUntil);
        });
    }

    public boolean isBlocked() {
        Attempt attempt = attempts.get(clientKey());
        if (attempt == null || attempt.lockedUntil() == null) {
            return false;
        }
        if (Instant.now().isBefore(attempt.lockedUntil())) {
            return true;
        }
        attempts.remove(clientKey());
        return false;
    }

    private String clientKey() {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private record Attempt(int count, Instant lockedUntil) {
    }
}
