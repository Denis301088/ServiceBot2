package com.example.servicebot2.service;

import com.example.servicebot2.domain.Task;
import org.springframework.stereotype.Service;

@Service
public class BotService {


    private Action action;

    public void setAction(Action action) {
        this.action = action;
    }

    public Task perform(Task task){

         action.perform(task);
//         if(task.getStatusTask()==StatusTask.STOPPED) return task;
//         return finalTask(task);
        return task;
    }

    public Action getAction() {
        return action;
    }
}
