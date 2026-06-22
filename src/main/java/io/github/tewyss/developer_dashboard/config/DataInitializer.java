package io.github.tewyss.developer_dashboard.config;

import io.github.tewyss.developer_dashboard.user.User;
import io.github.tewyss.developer_dashboard.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.admin.username:}") String adminUsername,
                           @Value("${app.admin.password:}") String adminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return; // an account already exists — never re-seed or overwrite
        }

        if (!StringUtils.hasText(adminUsername) || !StringUtils.hasText(adminPassword)) {
            log.warn("No admin credentials configured — skipping initial account creation. "
                    + "Set ADMIN_USERNAME and ADMIN_PASSWORD to provision the first administrator.");
            return;
        }

        User admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .role("ROLE_ADMIN")
                .build();
        userRepository.save(admin);
        log.info("Created initial administrator account '{}'.", adminUsername);
    }
}
