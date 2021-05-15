package com.gmail.antoniomarkoski314.Chat.controllers;

import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.database.UserRepository;
import com.gmail.antoniomarkoski314.Chat.models.User;
import com.gmail.antoniomarkoski314.Chat.security.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository,
                          BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get-users")
    public List<User> getUsers(@RequestHeader Map<String, String> headers) {

            headers.forEach((key, value) -> {
                System.out.println(String.format("Header '%s' = %s", key, value));
            });

        return userRepository.findAll();
    }
    
    @GetMapping("/signup")
    public ResponseEntity register(@RequestHeader("Authorization") String header) {

        System.out.println("/signup @RequestHeader");

        Credentials credentials = getBasicCredentials(header);

        if (credentials == null) {
            System.out.println("No Basic header");
            return ResponseEntity.badRequest().build();
        }

        // Encode password before storing to the database
        String encodedPassword = passwordEncoder.encode(credentials.getPassword());
        credentials.setPassword(encodedPassword);
        User user = new User(credentials.getUsername(), credentials.getPassword(),"ROLE_USER","");

        // Store user to the database
        userRepository.save(user);

        return ResponseEntity.ok()
                //.headers(httpHeaders)
                .body(credentials);
    }

    @GetMapping("/login")
    public ResponseEntity authenticate(@RequestHeader("Authorization") String header) {

        System.out.println("/login @RequestHeader");
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("MyHeader", "MyHeader");
        return ResponseEntity.ok()
                .body(new Credentials("user", "user"));
    }

    @GetMapping("/get-role-user")
    public User getRoleUser() {

        User user = new User("user", "user","ROLE_USER","");

        return user;
    }

    @GetMapping("/get-role-admin")
    public User getRolrAdmin() {

        User user = new User("admin", "admin","ROLE_ADMIN","");

        return user;
    }

    // Get credentials from "Authorization" header
    private Credentials getBasicCredentials(String header) {

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {
            System.out.println(header);
            String headerValue = header.substring(Properties.BASIC_PREFIX.length());

            String decodedValue = new String(Base64.getDecoder().decode(headerValue));
            System.out.println(decodedValue);

            String[] substringArray = decodedValue.split(":");
            if (substringArray.length > 1) {
                return new Credentials(substringArray[0], substringArray[1]);
            }
        } else {
            System.out.println("Authentication is null or it's not Basic");
        }
        return null;
    }

}

