package com.example.servicebot2.service;


import com.example.servicebot2.domain.Task;


public interface Action {

    Task perform(Task task);

    Action create();

}
