package com.example.servicebot2.domain;

import java.util.Comparator;

public class ComparatorTime implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {

//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
//        LocalDateTime localDateTime1=LocalDateTime.parse(o1.getStopTime(),dateTimeFormatter);
//        LocalDateTime localDateTime2=LocalDateTime.parse(o2.getStopTime(),dateTimeFormatter);
//
//        return localDateTime1.compareTo(localDateTime2);
        return o1.getId().compareTo(o2.getId());

    }


}
