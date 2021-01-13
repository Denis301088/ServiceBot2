package com.example.servicebot2.repos;

import com.example.servicebot2.domain.Account;
import com.example.servicebot2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface UserRepos extends JpaRepository<User,Long> {

     User findByUsername(String username);

     User findByactivationCode(String activationCode);


}
