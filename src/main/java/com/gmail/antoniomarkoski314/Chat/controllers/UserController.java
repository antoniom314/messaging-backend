package com.gmail.antoniomarkoski314.Chat.controllers;

import com.gmail.antoniomarkoski314.Chat.Properties;
import com.gmail.antoniomarkoski314.Chat.repositories.UserRepository;
import com.gmail.antoniomarkoski314.Chat.models.Credentials;
import com.gmail.antoniomarkoski314.Chat.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
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

    @GetMapping(Properties.getUsersUrl)
    public List<User> getUsers(@RequestHeader Map<String, String> headers) {

        return userRepository.findAll();
    }
    
    @GetMapping(Properties.registerUrl)
    public ResponseEntity register(@RequestHeader(Properties.AUTHENTICATION_HEADER) String header) {

        Credentials credentials = getBasicCredentials(header);

        if (credentials == null) {
            System.out.println("No Basic header");
            return ResponseEntity.badRequest().build();
        }
        // Encode password before storing to the database
        String encodedPassword = passwordEncoder.encode(credentials.getPassword());
        credentials.setPassword(encodedPassword);

        User user = new User(credentials.getUsername(), credentials.getPassword(),"USER","");
        // Store user to the database
        userRepository.save(user);

        return ResponseEntity.ok()
                .body(credentials);
    }

    @GetMapping(Properties.authenticateUrl)
    public ResponseEntity authenticate() {
        return ResponseEntity.ok().build();
    }

    // Get credentials (username and password) from "Authorization" header
    private Credentials getBasicCredentials(String header) {

        if (header != null && header.startsWith(Properties.BASIC_PREFIX)) {

            String headerValue = header.substring(Properties.BASIC_PREFIX.length());
            String decodedValue = new String(Base64.getDecoder().decode(headerValue));

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

