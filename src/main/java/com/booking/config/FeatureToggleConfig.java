package com.booking.config;

import com.booking.toggles.Features;
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

import javax.sql.DataSource;
import java.nio.file.attribute.UserPrincipal;

@Configuration
public class FeatureToggleConfig implements TogglzConfig {

    @Autowired
    private DataSource dataSource;

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

                return new SimpleFeatureUser(userName, true);
            }

            protected String getUserName(Authentication authentication) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserPrincipal) {
                    UserPrincipal userPrincipal = (UserPrincipal) principal;
                    return userPrincipal.getName();
                }
                return principal.toString();
            }
        };
    }
}

