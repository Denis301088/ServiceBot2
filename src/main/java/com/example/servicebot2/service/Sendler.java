package com.example.servicebot2.service;

import com.example.servicebot2.domain.StatusTask;
import com.example.servicebot2.domain.Task;
import com.example.servicebot2.repos.AccountRepos;
import com.example.servicebot2.repos.TaskRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Future;


@Service
@Scope(value = "prototype")
public class Sendler {

    @Autowired
    private BotService botService;

    private List<Future>taskList=new Vector<>();

    @Autowired
    TaskRepos taskRepos;

    @Autowired
    AccountRepos accountRepos;

    @Async
    public Future<Task> execute(Task task){

//        taskList.add(task);
        Future<Task> asyncResult=null;
        try{
            asyncResult = new AsyncResult<Task>(task);
            taskList.add(asyncResult);
            Action action=task.getAct().getAction().create();
            botService.setAction(action);
            botService.perform(task);
//        if(taskList.contains(asyncResult))taskList.remove(asyncResult);
            if(task.getStatusTask()!=StatusTask.STOPPED && taskList.contains(asyncResult)){
                task.setStatusTask(StatusTask.STOPPED);
                this.finalTask(task);
                taskList.remove(asyncResult);
            }
        }catch (Exception ex){
            if(task.getStatusTask()!=StatusTask.STOPPED && taskList.contains(asyncResult)){
                task.setStatusTask(StatusTask.ERROR);
                this.finalTask(task);
                taskList.remove(asyncResult);

            }

            ex.printStackTrace();
//            new Exception("Ошибка WebDriver").printStackTrace();//логировать
        }


        return asyncResult;
    }

    synchronized private Task finalTask(Task task){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime localDateTime=LocalDateTime.now();
        task.setStopTime(localDateTime.format(dateTimeFormatter));
        accountRepos.save(task.getAccount());
        return taskRepos.save(task);

    }

    public List<Future> getTaskList() {
        return taskList;
    }

    public BotService getBotService() {
        return botService;
    }
}
