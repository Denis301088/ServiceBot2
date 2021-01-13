package com.example.servicebot2.domain;

import com.example.servicebot2.botaction.Likes;
import com.example.servicebot2.botaction.LikesSubscriptions;
import com.example.servicebot2.botaction.Subscriptions;
import com.example.servicebot2.botaction.Unsubscribe;
import com.example.servicebot2.service.Action;


public enum Act {

    LIKE(new Likes(),"Лайк"),
    SUBSCRIPTION(new Subscriptions(),"Подписка"),
    LIKEPLUSSUBSCRIPTION(new LikesSubscriptions(),"Лайк + Подписка"),
    UNSUBSCRIBE(new Unsubscribe(),"Отписка");

    String nameAct;

    Action action;

    Act(Action action,String nameAct) {
        this.action = action;
        this.nameAct=nameAct;
    }

    public Action getAction() {
        return action;
    }

    @Override
    public String toString() {
        return nameAct;
    }
}



