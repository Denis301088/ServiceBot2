package com.example.servicebot2.service;


import com.example.servicebot2.domain.Role;
import com.example.servicebot2.domain.User;
import com.example.servicebot2.repos.UserRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepos userRepos;

    @Autowired
    MailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SessionRepository sessionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepos.findByUsername(username);
        if(user==null){

            throw new UsernameNotFoundException("Неверно введен логин или пароль");
        }
        return user;
    }

    public boolean addUser(User user)throws MailSendException {

        User userFromDb=userRepos.findByUsername(user.getUsername());
        if(userFromDb!=null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());//Активационный код для email
        user.setPassword(passwordEncoder.encode(user.getPassword()));//Добавить потом Шифрование!!!
//        userRepos.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) throws MailSendException {
        if(!StringUtils.isEmpty(user.getUsername())){

            String message=String.format("Привет! \n" + "Добро пожаловать в ServiceBot. Пожалуйста, перейдите по ссылке для подтверждения вашей почты\r\n" + "http://localhost:8080/activate/%s",
                    user.getActivationCode());
            mailSender.send(user.getUsername(),"Ссылка для активации аккаунта",message);

        }
    }

    public boolean activateUserCode(String code){

        User user=userRepos.findByactivationCode(code);

        if(user==null){
           return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepos.save(user);
        return true;
    }
}
