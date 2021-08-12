package com.gmail.antoniomarkoski314.Chat.services.userdetails;

import com.gmail.antoniomarkoski314.Chat.repositories.UserRepository;
import com.gmail.antoniomarkoski314.Chat.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("username= " + username);
        }

        return new UserDetailsImpl(user);
    }
}
