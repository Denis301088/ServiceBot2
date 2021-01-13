package com.example.servicebot2.controller;

import com.example.servicebot2.domain.Account;
import com.example.servicebot2.domain.User;
import com.example.servicebot2.repos.AccountRepos;
import com.example.servicebot2.repos.UserRepos;
import com.example.servicebot2.service.UserService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {

    @Value("${instagram.path}")
    String instagramPath;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    UserRepos userRepos;

    @Autowired
    AccountRepos accountRepos;


    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){

//        model.addAttribute("username",user.getUsername());
//        model.addAttribute("email",user.getEmail());
        List<Account> accountList=accountRepos.findByClient(user);
        model.addAttribute("accounts",accountList);

        return "profile";
    }

    @PostMapping("/profile")
    public String newPassword(@RequestParam String password,
                              @RequestParam String password2,
                              @RequestParam String password3,
                              @AuthenticationPrincipal User user,
                              Model model){


        if(StringUtils.isEmpty(password)||StringUtils.isEmpty(password2)||StringUtils.isEmpty(password3)){

            if(StringUtils.isEmpty(password)){
                model.addAttribute("passwordError","Введите текущий пароль!");
            }
            if(StringUtils.isEmpty(password2)){
                model.addAttribute("password2Error","Это поле не должно быть пустым!");
            }
            if(StringUtils.isEmpty(password3)){
                model.addAttribute("password3Error","Это поле не должно быть пустым!");
            }

            return "profile";

        }

        if(password2.length()<6){
            model.addAttribute("messageType","danger");
            model.addAttribute("message","Новый пароль должен содержать не менее 6 символов!");
            return "profile";
        }

        if(passwordEncoder.matches(password,user.getPassword())){

            if(password2.equals(password3)){
                user.setPassword(passwordEncoder.encode(password2));
                userRepos.save(user);
                model.addAttribute("messageType","success");
                model.addAttribute("message","Пароль от личного кабинета изменен.");
            }else {
                model.addAttribute("messageType","danger");
                model.addAttribute("message","Пароли не совпадают!");
            }
        }else {
            model.addAttribute("messageType","danger");
            model.addAttribute("message","Текущий пароль введен неверно!");
        }

        model.addAttribute("accounts",user.getAccounts());

        return "profile";
    }



}
