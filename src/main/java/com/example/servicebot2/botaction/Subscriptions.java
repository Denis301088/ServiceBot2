package com.example.servicebot2.botaction;

import com.example.servicebot2.domain.StatusTask;
import com.example.servicebot2.domain.Task;
import com.example.servicebot2.templateaction.TemplateActionInstagram;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Subscriptions extends TemplateActionInstagram {


    @Override
    public Task perform(Task task) {


        int count=0;

        try(PrintWriter printWriter=new PrintWriter("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\errors.txt")) {
            List<String>usersIgnore=task.getFilter().usersIgnore(task);

            ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника

            List<WebElement> webElements=this.getSuscribers(driver);//захожу в окно подписчиков

            Actions actions = new Actions(driver);

            Set<String>recurringAccount=new HashSet<>();

            for(int i=0; i<webElements.size() && task.getPresentCountSubscriptions()!=Integer.parseInt(task.getCountSubscriptions()); i++){

                try{

                    if(task.getStatusTask()==StatusTask.STOPPED)break;//Остановка задания
                    //================Фильтр по аккаунам на которые выполнялось действие
                    String href= webElements.get(i).findElement(By.tagName("a")).getAttribute("href");

                    String text=webElements.get(i).findElement(By.tagName("button")).getText();
                    if(task.getFilter().methodFilter_OldAccounts(task,href)||text.equals("Подписки")||text.equals("Запрос отправлен") || usersIgnore.contains(href) || recurringAccount.contains(href)){//

                        webElements=this.scrolling(driver,webElements,i);
                        continue;
                    }
                    recurringAccount.add(href);

                    //================

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actions.moveToElement(webElements.get(i).findElement(By.tagName("span"))).build().perform();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    WebElement element = driver.findElement(By.tagName("body")).findElement(By.xpath("/html/body/div[5]"));

                    System.out.println(href);
                    System.out.println(task.getAccount().getLogin());
                    if(!task.getFilter().firstFilterAccounts(driver,element)){

                        actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/div/div"))).build().perform();
                        webElements=this.scrolling(driver,webElements,i);
                        continue;

                    }

                    this.randomThreadPause(25,35);


                    webElements.get(i).findElement(By.tagName("a")).click();/*findElement(By.tagName("a"))*/;


                    if(task.getFilter().filterAccounts(driver)){

                        this.randomThreadPause(18,35);

                        List<WebElement> buttons=driver.findElements(By.tagName("button"));//Нажимаю подписаться
                        for(WebElement button:buttons){

                            if(button.getText().equals("Подписаться")){
                                button.click();
                                task.getAccount().getMemoryLinksProfiles().add(href);
                                task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
                                break;
                            }
                        }

                    }


                    System.out.println("---------------------------------------------");


                    this.randomThreadPause(10,15);
                    driver.navigate().back();//Было внутри метода scrolling.Вытащил сюда для работы метода methodFilter_OldAccounts.Возможно что то сломается,тогда вернуть

                    webElements = this.scrolling(driver, webElements, i);

                }catch (Exception ex){

                    if(count==10){
                        break;
                    }
                    count++;

                    printWriter.println("==============================================");
                    printWriter.println("count= "+ count);
                    printWriter.println("Account: "+task.getAccount().getLogin());
                    printWriter.println(task.getPresentCountSubscriptions());
                    printWriter.println(ex.getStackTrace());
                    printWriter.println("==============================================");

                    driver.get("https://www.instagram.com/"+task.getUserLogins().trim()+"/");

                    WebElement webElement=driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div"));

                    webElement.findElement(By.tagName("ul")).findElements(By.tagName("li")).get(1).click();//PZuss// AJAX элементы искать только через тэги!!!!!!!!!

                    webElements=driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));//получаю первые 12 элементов

                    i=0;

                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return task;
    }

    @Override
    public synchronized Subscriptions create(){
        return new Subscriptions();
    }


//=============================================    Законсервировано     ==========================================================
//    @Override
//    public Task perform(Task task) {
//
//        ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника
//
//        List<String> listLogins = Arrays.asList(task.getUserLogins().split("\r\n"));
//
//        if(listLogins.size()==1){
//            return subscriptionOneSource(task,driver,listLogins.get(0));
//        }
//        else {
//            return subscriptionLotSource(task,listLogins,driver);
//        }
//
//
//    }
//
//    public Task subscriptionOneSource(Task task,ChromeDriver driver,String profile){
//
//
////        ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника
//
//        driver.get(instagramPath+"/"+profile+"/");
//        List<WebElement> webElements=this.getSuscribers(driver);//захожу в окно подписчиков
//
//        this.randomThreadPause(6,8);
//
//        for(int i=0; i<webElements.size() && task.getPresentCountSubscriptions( )!=Integer.parseInt(task.getCountSubscriptions()); i++){
//
//            if(task.getStatusTask()==StatusTask.STOPPED)break;//Остановка задания
//            //================Фильтр по аккаунам на которые выполнялось действие
//            String href= webElements.get(i).findElement(By.tagName("a")).getAttribute("href");
//            if(task.getFilter().methodFilter_OldAccounts(task,href)){//
//
//                webElements=this.scrolling(driver,webElements,i);//делаю скроллинг на случай если будет много подряд профилей на пропуск
//                continue;
//            }
//            task.getAccount().getMemoryLinksProfiles().add(href);
//            //================
//
//            this.randomThreadPause(14,18);
//
//            webElements.get(i).findElement(By.tagName("a")).click();//Захожу к подписчику
//
//            this.randomThreadPause(5,8);
//
//            if(task.getFilter().filterAccounts(driver)){
//
//                this.randomThreadPause(6,8);
//
//                List<WebElement> buttons=driver.findElements(By.tagName("button"));//Нажимаю подписаться
//                for(WebElement button:buttons){
//
//                    if(button.getText().equals("Запрос отправлен") || button.getText().equals("Отправить сообщение") || button.getText().equals("Отписаться")){
//
//                        break;
//                    }
//                    if(button.getText().equals("Подписаться")){
//                        button.click();
//                        System.out.println(task.getPresentCountSubscriptions());
//                        task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
//                        break;
//                    }
//                }
//
//
//            }
//
//            this.randomThreadPause(8,12);
//            driver.navigate().back();//Было внутри метода scrolling.Вытащил сюда для работы метода methodFilter_OldAccounts.Возможно что то сломается,тогда вернуть
//
//            webElements=this.scrolling(driver,webElements,i);
//
//        }
//
//
//
//        return task;
//    }
//
//    Task subscriptionLotSource(Task task, List<String> listLogins,ChromeDriver driver){
//
//
//        this.randomThreadPause(6, 8);
//
//        for (int i = 0;/* i < webElements.size() && */task.getPresentCountSubscriptions() != Integer.parseInt(task.getCountSubscriptions()); i++) {
//            if (task.getStatusTask() == StatusTask.STOPPED) break;
//            Iterator<String> iterator=listLogins.iterator();
//
//            while (iterator.hasNext()){
//
//                this.randomThreadPause(2, 4);
//                String profile=iterator.next();
//                if(listLogins.size()>1){
//                    driver.get(instagramPath+"/"+profile+"/");
//                }else {
//                    driver.get(instagramPath+"/"+profile+"/");
//                    continue;
//                }
//
//
//                List<WebElement> webElements = this.getSuscribers(driver);//захожу в окно подписчиков
//                webElements = this.scrolling(driver, webElements, i);
//
//                if(i>webElements.size()){
//                    iterator.remove();
//                    continue;
//                }
//                if (task.getStatusTask() == StatusTask.STOPPED) break;//Остановка задания
//                //================Фильтр по аккаунам на которые выполнялось действие
//                String href = webElements.get(i).findElement(By.tagName("a")).getAttribute("href");
//                if (task.getFilter().methodFilter_OldAccounts(task, href)) {//
//
//                    webElements = this.scrolling(driver, webElements, i);
//                    continue;
//                }
//                task.getAccount().getMemoryLinksProfiles().add(href);
//                //================
//
//                this.randomThreadPause(14, 18);
//
//                webElements.get(i).findElement(By.tagName("a")).click();//Захожу к подписчику
//
//                this.randomThreadPause(5, 8);
//
//                if (task.getFilter().filterAccounts(driver)) {
//
//                    this.randomThreadPause(6, 8);
//
//                    List<WebElement> buttons = driver.findElements(By.tagName("button"));//Нажимаю подписаться
//                    for (WebElement button : buttons) {
//
//                        if (button.getText().equals("Запрос отправлен") || button.getText().equals("Отправить сообщение") || button.getText().equals("Отписаться")) {
//
//                            break;
//                        }
//                        if (button.getText().equals("Подписаться")) {
//                            button.click();
//                            System.out.println(task.getPresentCountSubscriptions());
//                            task.setPresentCountSubscriptions(task.getPresentCountSubscriptions() + 1);
//                            break;
//                        }
//                    }
//
//
//                }
//
//                this.randomThreadPause(8, 12);
//                driver.navigate().back();//Было внутри метода scrolling.Вытащил сюда для работы метода methodFilter_OldAccounts.Возможно что то сломается,тогда вернуть
//
//
//            }
//
//        }
//        return task;
//    }



}
