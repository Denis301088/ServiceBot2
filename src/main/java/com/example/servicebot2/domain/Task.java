package com.example.servicebot2.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Entity
public class Task implements Comparable<Task> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

//    @CreationTimestamp
//    LocalDateTime

    private String startTime;

    private String stopTime;

    @Enumerated(EnumType.STRING)
    private Act act;

    @NotBlank(message = "Введите логины пользователей-источников!")
    private String userLogins;

    @NotBlank(message = "Введите необходимое количество подписок/ лайков/ лайков+подписок!")
    private String countSubscriptions;

    private int presentCountSubscriptions;

    private Filter filter;

    private FilterUnsubscribe filterUnsubscribe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @NotNull(message="Выберите аккаунт для продвижения!")
    Account account;

    private volatile StatusTask statusTask;


    public Task(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public Act getAct() {
        return act;
    }

    public void setAct(Act act) {
        this.act = act;
    }

    public String getUserLogins() {
        return userLogins;
    }

    public void setUserLogins(String userLogins) {
        this.userLogins = userLogins;
    }

    public String getCountSubscriptions() {
        return countSubscriptions;
    }

    public void setCountSubscriptions(String countSubscriptions) {
        this.countSubscriptions = countSubscriptions;
    }

    public int getPresentCountSubscriptions() {
        return presentCountSubscriptions;
    }

    public void setPresentCountSubscriptions(int presentCountSubscriptions) {
        this.presentCountSubscriptions = presentCountSubscriptions;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public FilterUnsubscribe getFilterUnsubscribe() {
        return filterUnsubscribe;
    }

    public void setFilterUnsubscribe(FilterUnsubscribe filterUnsubscribe) {
        this.filterUnsubscribe = filterUnsubscribe;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public StatusTask getStatusTask() {
        return statusTask;
    }

    public void setStatusTask(StatusTask statusTask) {
        this.statusTask = statusTask;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Task o) {
        return this.id-o.getId();
    }
}
