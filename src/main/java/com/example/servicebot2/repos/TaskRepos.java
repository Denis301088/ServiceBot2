package com.example.servicebot2.repos;

import com.example.servicebot2.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepos extends JpaRepository<Task,Integer> {

}
