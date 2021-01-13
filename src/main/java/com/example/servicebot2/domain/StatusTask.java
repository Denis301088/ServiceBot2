package com.example.servicebot2.domain;

public enum StatusTask {
    WORK("Работает"),
    STOPPED("Завершено"),
    ERROR("Завершено с ошибкой");

    private String status;
    private StatusTask(String status){
        this.status=status;
    }

    @Override
    public String toString() {
        return status;
    }
}
