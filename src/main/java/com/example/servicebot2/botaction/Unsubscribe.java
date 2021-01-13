package com.example.servicebot2.botaction;

import com.example.servicebot2.domain.*;
import com.example.servicebot2.templateaction.TemplateActionInstagram;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Unsubscribe extends TemplateActionInstagram {


    @Override
    public Task perform(Task task) {

        List<String>usersIgnore=task.getFilter().usersIgnore(task);

        ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника

        List<WebElement> webElementsSubscription=getSuscribers(driver);

        scr(driver,webElementsSubscription,task);


//        this.randomThreadPause(9,13);

        if(is_Filter(task)&& task.getFilterUnsubscribe().getUnsubscribeFrom()!=UnsubscribeFrom.PRIVATE_NOTACCEPTED){

            return unsubscribeNonFilterReciprAndNonrecipr(driver,task,webElementsSubscription,usersIgnore);

        }else {
            if(task.getFilterUnsubscribe().getUnsubscribeFrom()!=UnsubscribeFrom.PRIVATE_NOTACCEPTED){

                for(int i=0; i<webElementsSubscription.size() && task.getPresentCountSubscriptions()!=Integer.parseInt(task.getCountSubscriptions()); i++){


                    String href= webElementsSubscription.get(i).findElement(By.tagName("a")).getAttribute("href");
                    boolean isReciprocal=task.getAccount().getMemoryLinksProfiles().contains(href);
                    if(task.getStatusTask() == StatusTask.STOPPED)break;//Остановка задания
                    if(usersIgnore.isEmpty() && usersIgnore.contains(href))continue;
                    if(task.getFilterUnsubscribe().getUnsubscribeFrom()==UnsubscribeFrom.NONRECIPROCAL && isReciprocal|| task.getFilterUnsubscribe().getUnsubscribeFrom()==UnsubscribeFrom.RECIPROCAL && !isReciprocal)continue;

                    webElementsSubscription.get(i).findElement(By.tagName("a")).click();//Захожу к подписчику
                    if(task.getFilter().filterAccounts(driver)){

                        driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/section/div[1]/div[1]/div/div[2]/div/span/span[1]/button")).click();
                        driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[3]/button[1]")).click();
                        task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);

                    }

                    driver.navigate().back();
                    webElementsSubscription=this.scrolling(driver,webElementsSubscription,i);

                }
            }else {
                List<String>listProfiles=task.getAccount().getMemoryLinksProfiles().stream().collect(Collectors.toList());

                for(int i=0; i<webElementsSubscription.size() && task.getPresentCountSubscriptions()!=Integer.parseInt(task.getCountSubscriptions()); i++){
                    if(task.getStatusTask() == StatusTask.STOPPED)break;//Остановка задания
                    if(usersIgnore.isEmpty() && usersIgnore.contains(listProfiles.get(i)))continue;
                    driver.get(listProfiles.get(i));
                    if(task.getFilter().filterAccounts(driver)){
                        List<WebElement> buttons=driver.findElements(By.tagName("button"));
                        for(WebElement button:buttons){

                            if(button.getText().equals("Запрос отправлен")){
                                button.click();
                                driver.findElement(By.xpath("/html/body/div[4]/div/div/div/div[3]/button[1]")).click();
                                task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
                                break;
                            }
                        }
                    }

                }
            }
            return task;


        }

    }

    @Override
    public synchronized Unsubscribe create() {
        return new Unsubscribe();
    }

    List<WebElement>scr(ChromeDriver driver, List<WebElement> webElements, Task task){
        for (int i=0;i<webElements.size();i++){

//            webElements=driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));
            if(i==webElements.size()-1){//изменил == на >

                WebElement el=webElements.get(webElements.size()-1).findElement(By.tagName("a"));
                JavascriptExecutor je = (JavascriptExecutor) driver;
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                je.executeScript("arguments[0].scrollIntoView(true);",el);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                webElements=driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));
            }

        }
        return webElements;
    }

    @Override
    public ChromeDriver initializationDriver(Task task) {
        System.setProperty ("webdriver.chrome.driver", "C:\\chromedriver.exe");//C:\Users\RET\.m2\repository\org\seleniumhq\selenium\selenium-chrome-driver\3.141.59\selenium-chrome-driver-3.141.59.jar

        ChromeDriver driver=new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);

        driver.get(instagramPath);

        Set<Cookie> cookies=task.getAccount().getCookies();

        for (Cookie cookie:cookies){

            driver.manage().addCookie(cookie);
        }
        String currentUserPath=instagramPath+"/"+task.getAccount().getLogin().trim()+"/";

        driver.get(currentUserPath);
        driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div"));//Ищу элементы из аккаунта чтобы не ставить в паузу после нажатия кнопки

        task.getAccount().setCookies(driver.manage().getCookies());//сохраняю обновленные куки


        return driver;
    }

    @Override
    public List<WebElement>getSuscribers(ChromeDriver driver){

        WebElement webElement=driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div"));

        webElement.findElement(By.tagName("ul")).findElements(By.tagName("li")).get(2).click();//PZuss// AJAX элементы искать только через тэги!!!!!!!!!

        return driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));

    }

    private void unsubscribe(WebElement element,ChromeDriver driver,Task task){
        element.findElement(By.tagName("button")).click();
        this.randomThreadPause(18,22);
        driver.findElement(By.xpath("/html/body/div[5]/div/div/div/div[3]/button[1]")).click();
        task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
        this.randomThreadPause(13,17);
    }

    private boolean is_Filter(Task task){
        Filter filter=new Filter();
        filter.setAlienAccount(false);
        filter.setAvatar(false);
        filter.setCommercialAccount(false);
        filter.setMax_publication("");
        filter.setMin_publication("");
        filter.setMax_subscribers("");
        filter.setMin_subscribers("");
        filter.setMax_subscription("");
        filter.setMin_subscription("");
        filter.setDate_publication("");
        filter.setStopWords("");
        filter.setWhiteWords("");
        filter.setGender(Gender.NOGENDER);
        return filter.equals(task.getFilter());

    }


    Task unsubscribeNonFilterReciprAndNonrecipr(ChromeDriver driver,Task task,List<WebElement>webElementsSubscription,List<String>usersIgnore){

        for(int i=0; i<webElementsSubscription.size() && task.getPresentCountSubscriptions()!=Integer.parseInt(task.getCountSubscriptions()); i++){

            String href= webElementsSubscription.get(i).findElement(By.tagName("a")).getAttribute("href");
            boolean isReciprocal=task.getAccount().getMemoryLinksProfiles().contains(href);
            if(!usersIgnore.isEmpty() && usersIgnore.contains(href))continue;
            if(task.getFilterUnsubscribe().getUnsubscribeFrom()==UnsubscribeFrom.NONRECIPROCAL && isReciprocal || task.getFilterUnsubscribe().getUnsubscribeFrom()==UnsubscribeFrom.RECIPROCAL && !isReciprocal)continue;
            unsubscribe(webElementsSubscription.get(i),driver,task);
            webElementsSubscription=this.scrolling(driver,webElementsSubscription,i);
            this.randomThreadPause(16,20);
        }

        return task;
    }


}

