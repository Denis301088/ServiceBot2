package com.example.servicebot2.repos;

import com.example.servicebot2.domain.Account;
import com.example.servicebot2.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountRepos extends JpaRepository<Account,Long> {

    List<Account> findByClient(User user);
}
