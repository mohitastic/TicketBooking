package com.booking;

import com.booking.users.repository.UserRepository;
import com.booking.users.repository.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.booking.users.Role.Code.ADMIN;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (repository.findByUsername("seed-user-1").isEmpty()) {
                repository.save(new User("seed-user-1", "foobar", ADMIN));
            }
            if (repository.findByUsername("seed-user-2").isEmpty()) {
                repository.save(new User("seed-user-2", "foobar", ADMIN));
            }

        };
    }
}
