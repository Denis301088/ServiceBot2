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

public class LikesSubscriptions extends TemplateActionInstagram {


    @Override
    public Task perform(Task task) {

        int count=0;

        try(PrintWriter printWriter=new PrintWriter("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\errors.txt")){

            List<String>usersIgnore=task.getFilter().usersIgnore(task);

            ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника

            List<WebElement> webElements=this.getSuscribers(driver);//захожу в окно подписчиков

            Actions actions = new Actions(driver);

            Set<String> recurringAccount=new HashSet<>();

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

                    //================
                    recurringAccount.add(href);

                    Thread.sleep(2000);
                    actions.moveToElement(webElements.get(i).findElement(By.tagName("span"))).build().perform();
                    Thread.sleep(2000);

                    WebElement element = driver.findElement(By.tagName("body")).findElement(By.xpath("/html/body/div[5]"));

                    boolean isLike=filterForLikes(element);

                    System.out.println(href);
                    if(!task.getFilter().firstFilterAccounts(driver,element)){

                        actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/div/div"))).build().perform();
                        webElements=this.scrolling(driver,webElements,i);
                        continue;

                    }

                    this.randomThreadPause(14,24);

                    webElements.get(i).findElement(By.tagName("a")).click();/*findElement(By.tagName("a"))*/;

                    if(task.getFilter().filterAccounts(driver)){

                        this.randomThreadPause(16,30);


                        List<WebElement> buttons=driver.findElements(By.tagName("button"));//Нажимаю подписаться
                        for(WebElement button:buttons){

                            if(button.getText().equals("Подписаться")){
                                button.click();
                                task.getAccount().getMemoryLinksProfiles().add(href);
                                task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
                                break;
                            }
                        }

                        if(isLike){

                            this.randomThreadPause(8,14);
                            List<WebElement>listphoto=driver.findElement(By.className("ySN3v")).findElements(By.tagName("a"));

                            listphoto.get(0).click();

                            this.randomThreadPause(2,6);
                            driver.navigate().back();
                            this.randomThreadPause(4,7);
                            if(!driver.findElement(By.className("fr66n")).findElement(By.tagName("svg")).getAttribute("aria-label").equals("Не нравится")){
                                driver.findElements(By.className("fr66n")).get(0).click();//нажимаю лайк
                                task.getAccount().getMemoryLinksProfiles().add(href);
                                this.randomThreadPause(5,7);
                                driver.findElement(By.xpath("/html/body/div[4]/div[3]/button")).click();//закрыть фото
                                this.randomThreadPause(3,5);
                            }

                        }

                    }

                    System.out.println("---------------------------------------------");

                    this.randomThreadPause(8,12);
                    driver.navigate().back();//Было внутри метода scrolling.Вытащил сюда для работы метода methodFilter_OldAccounts.Возможно что то сломается,тогда вернуть

                    webElements=this.scrolling(driver,webElements,i);
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

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        return task;

    }

    @Override
    public synchronized LikesSubscriptions create() {
        return new LikesSubscriptions();
    }
}
