package com.example.servicebot2.domain;

import org.openqa.selenium.Cookie;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Поле не может быть пустым!")
    private String login;

    @NotBlank(message = "Поле не может быть пустым!")
    @Transient
    private String password;

//    String proxy;

    @ElementCollection(targetClass = Cookie.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "account_cookies")
//    @Fetch(FetchMode.SUBSELECT)//отключ lazy подгрузку
    private Set<Cookie> cookies;

    @ManyToOne(fetch = FetchType.EAGER)///*,optional = false*/
    @JoinColumn(name="client_id")//Пока не прописал name= выдавал ошибку создания первичного ключа таблицы
    private User client;

//    @OrderBy("stopTime")
//    @SortComparator(ComparatorTime.class)
//    @OrderBy(clause = "id ASC")
//    @OrderColumn(name = "id")

//    @SortComparator(ComparatorTime.class)

    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY/*,orphanRemoval = false*/)
    private Set<Task>tasks;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(joinColumns = @JoinColumn(name = "account_id"))
    @Column(name="links_profiles")
    private Set<String> memoryLinksProfiles;

    public Account(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

//    public String getProxy() {
//        return proxy;
//    }
//
//    public void setProxy(String proxy) {
//        this.proxy = proxy;
//    }


    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public Set<String> getMemoryLinksProfiles() {
        return memoryLinksProfiles;
    }

    public void setMemoryLinksProfiles(Set<String> memoryLinksProfiles) {
        this.memoryLinksProfiles = memoryLinksProfiles;
    }
}
