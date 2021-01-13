package com.example.servicebot2.domain;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class FilterUnsubscribe {


    private UnsubscribeFrom unsubscribeFrom;

    public UnsubscribeFrom getUnsubscribeFrom() {
        return unsubscribeFrom;
    }

    public void setUnsubscribeFrom(UnsubscribeFrom unsubscribeFrom) {
        this.unsubscribeFrom = unsubscribeFrom;
    }

}
