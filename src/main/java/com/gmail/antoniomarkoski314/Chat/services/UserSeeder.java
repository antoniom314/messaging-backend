package com.gmail.antoniomarkoski314.Chat.services;

import com.gmail.antoniomarkoski314.Chat.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.gmail.antoniomarkoski314.Chat.models.User;

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

        User user = new User("user", "$2a$10$X58Jh4lw1aWPqjfPCFl21.N/yE80gIyiTsdFlTL0JkDDka8zQUQ4m",
                "USER","");
        User user1 = new User("user1", "user1","USER","");
        User user2 = new User("user2", "user2","USER","");
        User admin = new User("admin", "$2a$10$yqlTt.5Ak3X5QkWsrL74auyvVKajQ8mtEiaNpPMIneYKqXNmjd2uK",
                "ADMIN","");
        User userBasic = new User("useruser", "$2a$10$vwK5ZDpkadYk0OPtTwupTeFfsSi3QMNGhRLFXrQr/1Yyh9u.b3QX.",
                "USER","");

        List<User> users = Arrays.asList(user, user1, user2, admin, userBasic);

        System.out.println("ERROR");
        userRepository.saveAll(users);
    }
}
