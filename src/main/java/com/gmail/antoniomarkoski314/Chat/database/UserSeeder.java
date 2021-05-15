package com.gmail.antoniomarkoski314.Chat.database;

import com.gmail.antoniomarkoski314.Chat.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
public class UserSeeder implements CommandLineRunner {

    private UserRepository userRepository;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User("user", "user","ROLE_USER","");
        User user1 = new User("user1", "user1","ROLE_USER","");
        User user2 = new User("user2", "user2","ROLE_USER","");
        User admin = new User("admin", "$2a$10$yqlTt.5Ak3X5QkWsrL74auyvVKajQ8mtEiaNpPMIneYKqXNmjd2uK","ROLE_ADMIN","");
        User userBasic = new User("useruser", "$2a$10$vwK5ZDpkadYk0OPtTwupTeFfsSi3QMNGhRLFXrQr/1Yyh9u.b3QX.","ROLE_USER","");

        List<User> users = Arrays.asList(user, user1, user2, admin, userBasic);

        userRepository.saveAll(users);
    }
}
