package com.example.servicebot2.botaction;

import com.example.servicebot2.domain.StatusTask;
import com.example.servicebot2.domain.Task;
import com.example.servicebot2.templateaction.TemplateActionInstagram;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;


public class Likes extends TemplateActionInstagram {


    @Override
    public Task perform(Task task) {

        task.getFilter().setPrivateAccount(true);
        task.getFilter().setMin_publication("1");

        List<String>usersIgnore=task.getFilter().usersIgnore(task);

        ChromeDriver driver = this.initializationDriver(task);//В методе роинициализировал ChromeDriver,вошел в аккаунт,заполнил куки,зашел на страницу аккаунта источника

        List<WebElement>webElements=this.getSuscribers(driver);//захожу в подписчиков

        Actions actions = new Actions(driver);

        for(int i=0; i<webElements.size() && task.getPresentCountSubscriptions( )!=Integer.parseInt(task.getCountSubscriptions()); i++){


            if(task.getStatusTask()== StatusTask.STOPPED)break;//Остановка задания

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

            //================Фильтр по аккаунам на которые выполнялось действие
            String href= webElements.get(i).findElement(By.tagName("a")).getAttribute("href");

            if(task.getFilter().methodFilter_OldAccounts(task,href) || usersIgnore.contains(href)){//

                webElements=this.scrolling(driver,webElements,i);
                continue;
            }

            //================


            System.out.println(href);
            if(!filterForLikes(element)/*Проверка на закр акк и 0 публикаций*/ || !task.getFilter().firstFilterAccounts(driver,element)){

                actions.moveToElement(driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/header/div/div"))).build().perform();
                webElements=this.scrolling(driver,webElements,i);
                continue;

            }

            this.randomThreadPause(12,19);

            webElements.get(i).findElement(By.tagName("a")).click();//Захожу к подписчику

            if(task.getFilter().filterAccounts(driver)){

                this.randomThreadPause(8,16);
                List<WebElement>listphoto=driver.findElement(By.className("ySN3v")).findElements(By.tagName("a"));

//                int valuePublication = Integer.parseInt(driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div")).findElement(By.tagName("ul")).findElements(By.tagName("li")).get(0).findElement(By.tagName("span")).getText().replaceAll("\\D",""));//Количество подписчиков
                Actions actions1 = new Actions(driver);
                actions1.moveToElement(listphoto.get(0)).build().perform();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                listphoto.get(0).click();

//                    if(listphoto.size()>=3){
//                        listphoto.get(ThreadLocalRandom.current().nextInt(0,4)).click();
//                    }else {
//
//                    }
//                    if(listphoto.size()==1){
//                        listphoto.get(0).click();
//                    }else {
//                        listphoto.get(ThreadLocalRandom.current().nextInt(0,listphoto.size()-1)).click();
//                    }
                this.randomThreadPause(2,7);
                driver.navigate().back();
                this.randomThreadPause(4,7);
                if(!driver.findElement(By.className("fr66n")).findElement(By.tagName("svg")).getAttribute("aria-label").equals("Не нравится")){
                    driver.findElements(By.className("fr66n")).get(0).click();//нажимаю лайк
                    task.getAccount().getMemoryLinksProfiles().add(href);
                    task.setPresentCountSubscriptions(task.getPresentCountSubscriptions()+1);
                    this.randomThreadPause(5,7);
                    driver.findElement(By.xpath("/html/body/div[4]/div[3]/button")).click();//закрыть фото
                    this.randomThreadPause(3,5);
                }
                driver.findElement(By.xpath("/html/body/div[4]/div[3]/button")).click();//закрыть фото
                this.randomThreadPause(3,5);

            }

            System.out.println("---------------------------------------------");
            this.randomThreadPause(10,15);

            driver.navigate().back();//Было внутри метода scrolling.Вытащил сюда для работы метода methodFilter_OldAccounts.Возможно что то сломается,тогда вернуть

            webElements=scrolling(driver,webElements,i);

        }

        return task;
    }

    @Override
    public synchronized Likes create() {
        return new Likes();
    }
}
