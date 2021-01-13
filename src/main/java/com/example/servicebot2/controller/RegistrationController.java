package com.example.servicebot2.controller;


import com.example.servicebot2.domain.User;
import com.example.servicebot2.repos.UserRepos;
import com.example.servicebot2.service.MailSender;
import com.example.servicebot2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class RegistrationController {


    @Autowired
    private UserService userService;

    @Autowired
    UserRepos userRepos;

    @Autowired
    MailSender mailSender;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model){

        boolean isConfirmEmpty= StringUtils.isEmpty(passwordConfirm);
        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Поле не может быть пустым.");
        }
        else if(passwordConfirm.length()<1/*||!passwordConfirm.matches("[A-Za-z]")*/){//ОБРАБОТАТЬ ЕСЛИ ВВЕЛИ ПАРОЛЬ С ПРОБЕЛАМИ
            model.addAttribute("passwordError", "Пароль должен содержать не менее 8 символов,без пробелов,состоять из комбинации букви и цифр");
            isConfirmEmpty=true;
        }

        if(!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают.");
            model.addAttribute("password2Error", "Пароли не совпадают.");
//            model.addAttribute("user", null);


            return "registration";
        }

        if(isConfirmEmpty||bindingResult.hasErrors()){
            Map<String,String> mapErrors=ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(mapErrors);
//            model.addAttribute("user",user);//можно не добавлять
//            model.addAttribute("user", null);
            return "registration";
        }

        try{
            if(!userService.addUser(user)){

                model.addAttribute("usernameError","Пользователь с таким e-mail уже зарегистрирован.");
//            model.addAttribute("user", null);
                return "registration";
            }else{

                proxyFileRead(user);

            }

        } catch (MailSendException ex){

            model.addAttribute("errorNotValidEmail","Неверное имя Email");

            return "registration";

        } catch (FileNotFoundException ex){

            model.addAttribute("errorNotValidEmail","Ошибка регистрации.");

            return "registration";
        }

        userRepos.save(user);
        model.addAttribute("message","Код для активации аккаунта был отправлен на почтовый ящик "+ user.getUsername());
        model.addAttribute("messageType","success");

        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code,Model model){

        boolean isAktivated=userService.activateUserCode(code);
        if(isAktivated){

            model.addAttribute("messageType","success");
            model.addAttribute("message","Аккаунт успешно активирован. Добро пожаловать!");

        }else{

            model.addAttribute("messageType","danger");
            model.addAttribute("message","Код активации не совпадает с отправленным по почте!");
        }
        return "login";
    }

    @GetMapping("/newpass")
    public String newPassword(){


        return "newpassword";
    }

    @PostMapping("/newpass")
    public String changePassword(@RequestParam String username, Model model){

        if(StringUtils.isEmpty(username)){
            model.addAttribute("usernameError","Поле не может быть пустым.");
            return "newpassword";
        }
        User user=userRepos.findByUsername(username);
        if(!(user==null)){
            user.setActivationCode(UUID.randomUUID().toString());
            String message=String.format("Привет! \n" + "Вы запросили восстановление пароля для доступа к сервису. Перейдите по ссылке для сброса пароля от вашего аккаунта\r\n" +
                    "http://localhost:8080/newpass/%s\r\n"+
                    "Не переходите,если вы не отправляли это письмо, в этом случае пароль сброшен не будет.\r\n",
                    user.getActivationCode());
            mailSender.send(user.getUsername(),"Сброс пароля для восстановления доступа к сервису",message);

            model.addAttribute("messageType","success");
            model.addAttribute("messageEmail","Ссылка для сброса пароля была отправлена на ваш почтовый ящик.");
        }else {
            model.addAttribute("messageType","danger");
            model.addAttribute("messageEmail","Пользователь с таким Email не найден!");
        }

        return "newpassword";
    }

    @GetMapping("/newpass/{code}")
    public String activateNewPassword(Model model, @PathVariable String code){

        User user=userRepos.findByactivationCode(code);
        if(user!=null){
            user.setActivationCode(null);
            user.setActive(false);
            model.addAttribute("user",user);
            return "newpass";
        }else {
            model.addAttribute("messageType","danger");
            model.addAttribute("messageEmail","Код активации не совпадает с отправленным по почте!");
        }

        return "newpassword";
    }

    @PostMapping("/newpass/form")
    public String addNewPassword(@RequestParam("password2") String passwordConfirm,
                                 @Valid User user,
                                 BindingResult bindingResult,
                                 Model model){

        boolean isConfirmEmpty= StringUtils.isEmpty(passwordConfirm);
        if(isConfirmEmpty){
            model.addAttribute("password2Error", "Поле не может быть пустым.");
        }
        else if(passwordConfirm.length()<1/*||!passwordConfirm.matches("[A-Za-z]")*/){//ОБРАБОТАТЬ ЕСЛИ ВВЕЛИ ПАРОЛЬ С ПРОБЕЛАМИ
            model.addAttribute("passwordError", "Пароль должен содержать не менее 6 символов,без пробелов,состоять из комбинации букви и цифр");
            isConfirmEmpty=true;
        }

        if(!user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Пароли не совпадают.");
            model.addAttribute("password2Error", "Пароли не совпадают.");
//            model.addAttribute("user", null);

            return "newpass";
        }

        if(isConfirmEmpty||bindingResult.hasErrors()){
            Map<String,String> mapErrors=ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(mapErrors);
//            model.addAttribute("user",user);//можно не добавлять
//            model.addAttribute("user", null);
            return "newpass";
        }
        User user1=userRepos.findByUsername(user.getUsername());
        user1.setPassword(passwordEncoder.encode(user.getPassword()));
        user1.setActive(true);

        userRepos.save(user1);
        model.addAttribute("user",user1);


        return "redirect:/history";
    }

    static synchronized void proxyFileRead(User user) throws FileNotFoundException {

        Scanner scanner=new Scanner(new File("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\proxyservers.txt"));

        List<String> list=new ArrayList<>();
        if(scanner.hasNext()){
            while(scanner.hasNext()){
                if(list.isEmpty()){
                    user.setProxy(scanner.nextLine());
                    continue;
                }

                list.add(scanner.nextLine());
            }

        }else {
            scanner.close();
            throw new FileNotFoundException();
        }

        scanner.close();

        PrintWriter printWriter=new PrintWriter("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\proxyservers.txt");
        for (String s:list){
            printWriter.write(s+"\n");
        }

        printWriter.close();
    }


}
