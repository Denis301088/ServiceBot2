package com.example.servicebot2.controller;


import com.example.servicebot2.domain.*;
import com.example.servicebot2.repos.AccountRepos;
import com.example.servicebot2.repos.TaskRepos;
import com.example.servicebot2.repos.UserRepos;
import com.example.servicebot2.service.BotService;
import com.example.servicebot2.service.Sendler;
import com.example.servicebot2.service.WordsFileComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {


    @Autowired
    Sendler sendler;

    @Value("${instagram.path}")
    String instagramPath;

    @Autowired
    AccountRepos accountRepos;

    @Autowired
    BotService botService;

    @Autowired
    TaskRepos taskRepos;

    @Autowired
    UserRepos userRepos;

    @Autowired
    WordsFileComponent wordsFileComponent;


    @GetMapping
    public String main(HttpSession httpSession, @AuthenticationPrincipal User user, Model model){

        if(user!=null) return "redirect:/history";//Если пользователь авторизован,то главная страница истории

        return "begin";
    }

    @GetMapping("/history")//Отображение страницы истории заданий
    public String historyTask(@AuthenticationPrincipal User user,Model model) throws ExecutionException, InterruptedException {

        methodHistoryTasks(user,model);

        return "historytask";
    }

    @PostMapping("repeattask")//Повторить сразу задание из истории заданий нажатием одной кнопки
    public String repeatTask(@AuthenticationPrincipal User user,@RequestParam(name = "taskId")Task task, Model model) throws ExecutionException, InterruptedException {

        if(!sendler.getTaskList().isEmpty()){
            for (Future<Task>future:sendler.getTaskList()){
                if(future.get().getAccount().getId().equals(task.getAccount().getId())){//Проверка не выполняется ли в данный момент
                    //это задание,если выполняется,то вывожу на странице оповещение

                    model.addAttribute("messageErrorsRepeatTasks","Для аккаунта "+ future.get().getAccount().getLogin() +
                            " уже выполняется задание! Остановите выполнение или дождитесь окончания.");
                    methodHistoryTasks(user,model);
                    return "historytask";
                }

            }
        }

        //Создаю новое задание, на основе параметров вызываемого для повтора задания
        task.getAccount().getMemoryLinksProfiles().size();//???
        Task newTask=new Task();
        BeanUtils.copyProperties(task,newTask, "id","startTime","stopTime","presentCountSubscriptions","statusTask");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime localDateTime=LocalDateTime.now();
        newTask.setStartTime(localDateTime.format(dateTimeFormatter));

        newTask.setStatusTask(StatusTask.WORK);

        Task task1=taskRepos.save(newTask);
        task1.getFilter().setWordsFileComponent(wordsFileComponent);

        sendler.execute(task1);

        return "redirect:/history";
    }

    @PostMapping("stoptask")//Кнопка остановки задания
    public String stopTask(@RequestParam Integer stopId, Model model) throws ExecutionException, InterruptedException {

        Iterator<Future> iterator=sendler.getTaskList().iterator();
        
        while (iterator.hasNext()){

            Future<Task>taskFuture = iterator.next();
            //Находим по id задание,которое нужно остановить
            if(taskFuture.get().getId().equals(stopId) && taskFuture.get().getStatusTask()!=StatusTask.STOPPED
                    && taskFuture.get().getStatusTask()!= StatusTask.ERROR){//Integer.parseInt(stopId.replaceAll("\\D",""))//  Изменил

                taskFuture.get().setStatusTask(StatusTask.STOPPED);
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                LocalDateTime localDateTime=LocalDateTime.now();
                taskFuture.get().setStopTime(localDateTime.format(dateTimeFormatter));//Записываем время остановки здания
                accountRepos.save(taskFuture.get().getAccount());
                taskRepos.save(taskFuture.get());
                iterator.remove();

            }
        }

        return "redirect:/history";
    }

    @RequestMapping("/newtask")//Отображение страницы создания задания(переход со страницы истории кнопкой новое задание и с главной страницы)
    public String newTask(@AuthenticationPrincipal User user,@RequestParam(required = false)Task task, Model model){

        User currentUser=userRepos.findByUsername(user.getUsername());
        model.addAttribute("accounts", currentUser.getAccounts());

        if(task==null){//Заходим в случае перехода с главной страницы

            Set<Task>tasks=new HashSet<>();
            for (Account account:currentUser.getAccounts()){

                tasks.addAll(account.getTasks());

            }

            if(!tasks.isEmpty()){//Если список заданий не пуст,то достаю объект фильтра из последнего задания,чтобы присвоить некоторые из его параметров новому заданию для удобства

                Filter filter=tasks.stream().max(Task::compareTo).get().getFilter();

                task=new Task();
                task.setFilter(new Filter());
                BeanUtils.copyProperties(filter,task.getFilter(),"privateAccount","avatar","skipOldAccounts","gender","alienAccount","commercialAccount","filterUnsubscribe");
                task.getFilter().setAvatar(true);
                task.getFilter().setSkipOldAccounts(true);
                task.getFilter().setAlienAccount(true);
                task.getFilter().setCommercialAccount(true);

            }

        }

        model.addAttribute("task",task);

        return "newtask";
    }

    @PostMapping("/newtask")//Запуск нового задания
    public String addTask(@AuthenticationPrincipal User user, @Valid @ModelAttribute("updateTask") Task task,BindingResult bindingResult, Model model) throws ExecutionException, InterruptedException {

        if(!sendler.getTaskList().isEmpty()){
            for (Future<Task>future:sendler.getTaskList()){
                //Проверяем чтобы данное задание не выполнялось в даннй момент,если выполняется,то не даем запустить и выводим на странице предупреждение
                if(future.get().getAccount().getId().equals(task.getAccount().getId())){

                    model.addAttribute("messageErrorsTasks",new ArrayList<String>(){{add("Для аккаунта "+future.get().getAccount().getLogin()+" уже выполняется задание! Остановите выполнение или дождитесь окончания.");}});
                    User currentUser=userRepos.findByUsername(user.getUsername());
                    model.addAttribute("accounts",currentUser.getAccounts());
                    model.addAttribute("task",task);
                    return "newtask";
                }

            }
        }

        if(task.getAct()!=Act.UNSUBSCRIBE){//Если задание не на отписку,то проверяем валидность полей
            if(bindingResult.hasErrors()){

                List<String>errorsTask=bindingResult.getFieldErrors().stream().map(fielderror->fielderror.getDefaultMessage()).collect(Collectors.toList());
                model.addAttribute("errorsTask",errorsTask);
                model.addAttribute("task",task);
                User currentUser=userRepos.findByUsername(user.getUsername());
                model.addAttribute("accounts",currentUser.getAccounts());
                model.addAttribute("messageErrorsTasks",errorsTask);//Сообщение об ошибке в полях задания
                return "newtask";//При нажатии наза на страницу возврщает на эту же страницу (Попоробовать исправить логику)!!!!!!
            }

        }else {//Если задание на отписку,то заходим в этот участок
            if(task.getAccount()==null||task.getCountSubscriptions().isEmpty()){
                List<String>list=new ArrayList<>();
                if(task.getAccount()==null){
                    list.add("Выберите аккаунт для продвижения!");
                }
                if(task.getCountSubscriptions().isEmpty()){
                    list.add("Введите необходимое количество отписок!");
                }
                model.addAttribute("messageErrorsTasks",list);
                model.addAttribute("task",task);
                User currentUser=userRepos.findByUsername(user.getUsername());
                model.addAttribute("accounts",currentUser.getAccounts());
                return "newtask";
            }

            task.getFilter().setSkipOldAccounts(false);//Для отписки это поле ставлю в false

        }

        task.getAccount().getMemoryLinksProfiles().size();//для LAZY инициализации списка аккаунтов,на котор соверш действ


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        LocalDateTime localDateTime=LocalDateTime.now();
        task.setStartTime(localDateTime.format(dateTimeFormatter));//Указываю время начала выполнения задания

        task.setStatusTask(StatusTask.WORK);

        if(task.getAct()!=Act.UNSUBSCRIBE){
            task.setFilterUnsubscribe(null);
        }else{
            task.setUserLogins("-");

        }

        Task task1=taskRepos.save(task);
        task1.getFilter().setWordsFileComponent(wordsFileComponent);

        sendler.execute(task1);

        return "redirect:/history";
    }



//    @RequestMapping("/newtask")//Отображение страницы создания задания(переход со страницы истории и с главной страницы)
//    public String newTask(@AuthenticationPrincipal User user,@RequestParam(required = false)Task task, Model model){
//
//        User currentUser=userRepos.findByUsername(user.getUsername());
//        model.addAttribute("accounts", currentUser.getAccounts());
//        Set<Task>tasks=new HashSet<>();
//        if(task==null){
//
//            for (Account account:currentUser.getAccounts()){
//
//                tasks.addAll(account.getTasks());
//
//            }
//
//            if(!tasks.isEmpty()){
//
//                Filter filter=tasks.stream().filter(x->x.getAct()!=Act.UNSUBSCRIBE).max(Task::compareTo).get().getFilter();
//
//                task=new Task();
//                task.setFilter(new Filter());
//                BeanUtils.copyProperties(filter,task.getFilter(),"privateAccount","avatar","skipOldAccounts","gender","alienAccount","commercialAccount");
//                task.getFilter().setAvatar(true);
//                task.getFilter().setSkipOldAccounts(true);
//                task.getFilter().setAlienAccount(true);
//                task.getFilter().setCommercialAccount(true);
//
//                FilterUnsubscribe filterUnsubscribe=tasks.stream().filter(x->x.getAct()==Act.UNSUBSCRIBE).max(Task::compareTo).get().getFilterUnsubscribe();
//
//                task.setFilterUnsubscribe(new FilterUnsubscribe());
//                task.getFilterUnsubscribe().setUnsubscribeFrom(UnsubscribeFrom.NONRECIPROCAL);
//                task.getFilterUnsubscribe().setListUsersIgnore(filterUnsubscribe.getListUsersIgnore());
//
//            }
//           // currentUser.getAccounts().stream().collect(Collectors.toList());
////            Optional<Account>optionalAccount=currentUser.getAccounts().stream().max((x,y)->Collections.max(x.getTasks()).getId()-Collections.max(y.getTasks()).getId());
//            //Optional<Account>optionalAccount=currentUser.getAccounts().stream().max((x,y)->x.getTasks().stream().collect(Collectors.toList()).get(x.getTasks().size()-1).getId()-y.getTasks().stream().collect(Collectors.toList()).get(y.getTasks().size()-1).getId());
////            if(optionalAccount.isPresent() && !optionalAccount.get().getTasks().isEmpty()){//!!!!!!!!!!!!!!!!!!!!исправить в другой версии
//////                Filter filter=optionalAccount.get().getTasks().stream().collect(Collectors.toList()).get(optionalAccount.get().getTasks().size()-1).getFilter();
////                Filter filter=optionalAccount.get().getTasks().stream().max((x,y)->x.compareTo(y)).get().getFilter();
////
//        }else {
//
//            for (Account account:currentUser.getAccounts()){
//
//                tasks.addAll(account.getTasks());
//
//            }
//            if(!tasks.isEmpty()){
//                if(task.getAct()!=Act.UNSUBSCRIBE){
//
//                    FilterUnsubscribe filterUnsubscribe=tasks.stream().filter(x->x.getAct()==Act.UNSUBSCRIBE).max(Task::compareTo).get().getFilterUnsubscribe();
//                    task.setFilterUnsubscribe(new FilterUnsubscribe());
//                    task.getFilterUnsubscribe().setUnsubscribeFrom(UnsubscribeFrom.NONRECIPROCAL);
//                    task.getFilterUnsubscribe().setListUsersIgnore(filterUnsubscribe.getListUsersIgnore());
//
//                }else {
//                    Filter filter=tasks.stream().filter(x->x.getAct()!=Act.UNSUBSCRIBE).max(Task::compareTo).get().getFilter();
//                    task.setFilter(filter);
//                    task.getFilter().setAvatar(true);
//                    task.getFilter().setSkipOldAccounts(true);
//                    task.getFilter().setAlienAccount(true);
//                    task.getFilter().setCommercialAccount(true);
//                }
//
//            }
//
//
//        }
//
//        model.addAttribute("task",task);
//
//        return "newtask";
//    }




//    @PostMapping("/newtask")//Запуск нового задания
//    public String addTask(@AuthenticationPrincipal User user, @Valid @ModelAttribute("updateTask") Task task,BindingResult bindingResult, Model model) throws ExecutionException, InterruptedException {
//
//        if(!sendler.getTaskList().isEmpty()){
//            for (Future<Task>future:sendler.getTaskList()){
//                if(future.get().getAccount().getId()==task.getAccount().getId()){
//
//                    model.addAttribute("messageErrorsTasks",new ArrayList<String>(){{add("Для аккаунта "+future.get().getAccount().getLogin()+" уже выполняется задание! Остановите выполнение или дождитесь окончания.");}});
//                    User currentUser=userRepos.findByUsername(user.getUsername());
//                    model.addAttribute("accounts",currentUser.getAccounts());
//                    model.addAttribute("task",task);
//                    return "newtask";
//                }
//
//            }
//        }
////======================================Код для отписки======================================================
//        if(task.getAct()==Act.UNSUBSCRIBE){
//
//            if(task.getAccount()==null||task.getCountSubscriptions().isEmpty()){
//                List<String>list=new ArrayList<>();
//                if(task.getAccount()==null){
//                    list.add("Выберите аккаунт для продвижения!");
//                }
//                if(task.getCountSubscriptions().isEmpty()){
//                    list.add("Введите необходимое количество отписок!");
//                }
//                model.addAttribute("messageErrorsTasks",list);
//                model.addAttribute("task",task);
//                User currentUser=userRepos.findByUsername(user.getUsername());
//                model.addAttribute("accounts",currentUser.getAccounts());
//                return "newtask";
//            }
//
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
//            LocalDateTime localDateTime=LocalDateTime.now();
//            task.setStartTime(localDateTime.format(dateTimeFormatter));
//
//            task.setStatusTask(StatusTask.WORK);
//            task.getAccount().getMemoryLinksProfiles().size();//для LAZY инициализации списка аккаунтов,на котор соверш действ
//            task.setUserLogins(task.getAccount().getLogin());//аккаунт для отписки == аккаунту пользователей для действия
//
//            task.setFilter(new Filter());
////            Task newTask=new Task();
////            BeanUtils.copyProperties(task,newTask, "filter");
//
//            Task task1=taskRepos.save(task);
//
//
//            sendler.execute(task1);
//            return "redirect:/history";
//
////========================================================================================================================
//        }else {
//            if(bindingResult.hasErrors()){
//
//                List<String>errorsTask=bindingResult.getFieldErrors().stream().map(fielderror->fielderror.getDefaultMessage()).collect(Collectors.toList());
//                model.addAttribute("errorsTask",errorsTask);
//                model.addAttribute("task",task);
//                User currentUser=userRepos.findByUsername(user.getUsername());
//                model.addAttribute("accounts",currentUser.getAccounts());
//                model.addAttribute("messageErrorsTasks",errorsTask);//Сообщение об ошибке в полях задания
//                return "newtask";//При нажатии наза на страницу возврщает на эту же страницу (Попоробовать исправить логику)!!!!!!
//            }
//            task.getAccount().getMemoryLinksProfiles().size();//для LAZY инициализации списка аккаунтов,на котор соверш действ
//
//        }
//
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
//        LocalDateTime localDateTime=LocalDateTime.now();
//        task.setStartTime(localDateTime.format(dateTimeFormatter));
//
//        task.setStatusTask(StatusTask.WORK);
//
//        Task newTask=new Task();
//        BeanUtils.copyProperties(task,newTask, "filterUnsubscribe");
//
//        Task task1=taskRepos.save(newTask);
//        task1.getFilter().setWordsFileComponent(wordsFileComponent);
//
//        sendler.execute(task1);
//
//        return "redirect:/history";
//    }




    @PostMapping("/account")//Добавляю аккаунт
    public String addAccounts(@AuthenticationPrincipal User user,
                              @Valid Account account,
                              BindingResult bindingResult,
                              Model model) {

        User currentUser=userRepos.findByUsername(user.getUsername());

        if(currentUser.getAccounts().size()>3){
            model.addAttribute("messageErrorsTasks",new ArrayList<String>(){{add("Вы не можете добавить более 3-х аккаунтов для продвижения!");}});
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("account",account);
            return "newtask";
        }

        if(currentUser.getAccounts().stream().filter(x->x.getLogin().equals(account.getLogin())).findFirst().isPresent()){
            model.addAttribute("messageErrorsTasks",new ArrayList<String>(){{add("Этот аккаунт уже есть в списке добавленных аккаунтов!");}});
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("account",account);
            return "newtask";
        }

        if(bindingResult.hasErrors()){

            model.mergeAttributes(ControllerUtils.getErrors(bindingResult));
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("account",account);
            return "newtask";
        }


        if(account.getLogin().matches("(\\+?\\d+|.*@.*)")){
            model.addAttribute("messageErrorsTasks",new ArrayList<String>(){{add("В поле логин для добавления аккаунта используйте логин вашего профиля Instagram!");}});
//            User currentUser=userRepos.findByUsername(user.getUsername());
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("account",account);
            return "newtask";
        }

        ChromeDriver driver=null;
        try{

            ChromeOptions options=new ChromeOptions();
            String host=currentUser.getProxy();
            Proxy proxy=new Proxy().setHttpProxy(host).
                    setFtpProxy(host).
                    setSslProxy(host).setProxyType(Proxy.ProxyType.MANUAL);
            options.setHeadless(true);
            options.setProxy(proxy);
            System.setProperty ("webdriver.chrome.driver", "C:\\chromedriver.exe");//C:\Users\RET\.m2\repository\org\seleniumhq\selenium\selenium-chrome-driver\3.141.59\selenium-chrome-driver-3.141.59.jar

            driver=new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

            driver.get(instagramPath);

            List<WebElement>entrance=driver.findElements(By.tagName("input"));
            entrance.get(0).sendKeys(account.getLogin());
            entrance.get(1).sendKeys(account.getPassword());

            driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div/div[3]/button")).click();//Нажимаю кнопку вход

            driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div"));//Ищу элементы из аккаунта чтобы не ставить в паузу после нажатия кнопки

            driver.get(instagramPath +"/"+ account.getLogin());

            account.setCookies(driver.manage().getCookies());

            account.setClient(currentUser);//user??
            currentUser.getAccounts().add(account);

            accountRepos.save(account);


            model.addAttribute("isErrorAuthorization",true);
//            User currentUser=userRepos.findByUsername(user.getUsername());
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("messageAuthorization","Аккаунт добавлен.");
            model.addAttribute("account",null);

            return "newtask";

        }catch (Exception ex){


            model.addAttribute("isErrorAuthorization",false);
//            User currentUser=userRepos.findByUsername(user.getUsername());
            model.addAttribute("accounts",currentUser.getAccounts());
            model.addAttribute("messageAuthorization","Не удалось авторизоваться! Повторите попытку.");
            return "newtask";

        }
        finally {

//            driver.quit();

        }

    }


    public void methodHistoryTasks(@AuthenticationPrincipal User user, Model model) throws ExecutionException, InterruptedException {

        List<Future>futuresTask=sendler.getTaskList().stream().filter(x-> {
            try {
                return ((Task)x.get()).getAccount().getClient().getId().equals(user.getId());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());//Фильтрую из списка всех выполняющихся заданий выполняющиеся задания у данного пользователя


        Set<Task>tasks=new HashSet<>();

        for (Future<Task> future:futuresTask){

            System.out.println(tasks.add(future.get()));

            System.out.println("PresentCountSubscriptions: "+future.get().getPresentCountSubscriptions());
        }

        User currentUser=userRepos.findByUsername(user.getUsername());
        Set<Account>accounts=currentUser.getAccounts();
        for (Account account:accounts){//Достаю из базы данных всю историю задний для каждого аккаунта для отображения на страице истории заданий
            tasks.addAll(account.getTasks());
        }

        List<Task>list=tasks.stream().sorted().collect(Collectors.toList());
        Collections.reverse(list);

        System.out.println(futuresTask.size());

        //Публикую на странице отображения истории заданий список всех заданий,включая выполняемые в данный момент
        model.addAttribute("tasks",list);
    }
}






