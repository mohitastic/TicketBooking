package com.booking.config;

import com.booking.toggles.Features;
import com.booking.users.UserPrincipal;
import com.booking.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.togglz.core.Feature;
import org.togglz.core.manager.TogglzConfig;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.jdbc.JDBCStateRepository;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;
import static com.booking.users.Role.Code.ADMIN;

import javax.sql.DataSource;

@Configuration
public class FeatureToggleConfig implements TogglzConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    UserService userService;

    @Bean
    @Override
    public Class<? extends Feature> getFeatureClass() {
        return Features.class;
    }

    @Bean
    @Override
    public StateRepository getStateRepository() {
        return new JDBCStateRepository(dataSource);
    }

    @Bean
    @Override
    public UserProvider getUserProvider() {
        return new UserProvider() {
            @Override
            public FeatureUser getCurrentUser() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String userName = getUserName(authentication);
                boolean isAdmin = userService.fetchRole(userName).equals(ADMIN);
                return new SimpleFeatureUser(userName, isAdmin);
            }

            private String getUserName(Authentication authentication) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserPrincipal) {
                    UserPrincipal userPrincipal = (UserPrincipal) principal;
                    return userPrincipal.getUsername();
                }
                return principal.toString();
            }
        };
    }
}

