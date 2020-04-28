package com.booking;

import com.booking.users.User;
import com.booking.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local"})
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            if (repository.findByUserName("seed-user-1").isEmpty()) {
                repository.save(new User("seed-user-1", "foobar"));
            }
            if (repository.findByUserName("seed-user-2").isEmpty()) {
                repository.save(new User("seed-user-2", "foobar"));
            }
        };
    }
}