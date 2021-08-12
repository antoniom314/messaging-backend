package com.gmail.antoniomarkoski314.Chat.database;

import com.gmail.antoniomarkoski314.Chat.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
