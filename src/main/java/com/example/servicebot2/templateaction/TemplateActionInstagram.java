package com.example.servicebot2.templateaction;

import com.example.servicebot2.domain.Act;
import com.example.servicebot2.domain.Task;
import com.example.servicebot2.service.Action;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public abstract class TemplateActionInstagram implements Action {

    ThreadLocalRandom randomTime = ThreadLocalRandom.current();

    protected String instagramPath="https://www.instagram.com";


    public ChromeDriver initializationDriver(Task task) {

        String host=task.getAccount().getClient().getProxy();
        ChromeOptions options=new ChromeOptions();
        Proxy proxy=new Proxy().setHttpProxy(host).
                setFtpProxy(host).
                setSslProxy(host).setProxyType(Proxy.ProxyType.MANUAL);


//        options.setHeadless(true);
        options.setProxy(proxy);
        System.setProperty ("webdriver.chrome.driver", "C:\\chromedriver.exe");//C:\Users\RET\.m2\repository\org\seleniumhq\selenium\selenium-chrome-driver\3.141.59\selenium-chrome-driver-3.141.59.jar

        ChromeDriver driver=new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);

        driver.get(instagramPath);

        Set<Cookie> cookies=task.getAccount().getCookies();

        for (Cookie cookie:cookies){

            driver.manage().addCookie(cookie);
        }
        String currentUserPath=instagramPath+"/"+task.getUserLogins().trim()+"/";
//        String currentUserPath=instagramPath+"/"+task.getAccount().getLogin()+"/";


//        List<WebElement> entrance=driver.findElements(By.tagName("input"));
//        entrance.get(0).sendKeys(task.getAccount().getLogin());
//        entrance.get(1).sendKeys(task.getAccount().getPassword());
//        driver.findElement(By.xpath("//*[@id=\"loginForm\"]/div/div[3]/button")).click();//Нажимаю кнопку вход

        driver.get(currentUserPath);
        driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div"));//Ищу элементы из аккаунта чтобы не ставить в паузу после нажатия кнопки

        task.getAccount().setCookies(driver.manage().getCookies());//сохраняю обновленные куки

        


        return driver;
    }

    protected List<WebElement> getSuscribers(ChromeDriver driver){

        WebElement webElement=driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div"));

        webElement.findElement(By.tagName("ul")).findElements(By.tagName("li")).get(1).click();//PZuss// AJAX элементы искать только через тэги!!!!!!!!!

        return driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));

    }


    protected List<WebElement> scrolling(ChromeDriver driver,List<WebElement>webElements,int i){

        webElements=driver.findElement(By.className("PZuss")).findElements(By.tagName("li"));
        if(i!=0 && i%3==0){//изменил == на >

            WebElement el=webElements.get(i).findElement(By.tagName("a"));
            JavascriptExecutor je = (JavascriptExecutor) driver;
//            this.randomThreadPause(18,21);
            this.randomThreadPause(5,8);
            je.executeScript("arguments[0].scrollIntoView(true);",el);
//            this.randomThreadPause(12,16);
            this.randomThreadPause(5,8);

        }
        return webElements;

    }

    protected void randomThreadPause(int time1, int time2){

        try {
            Thread.sleep(randomTime.nextInt(time1*1000,time2*1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected boolean filterForLikes(WebElement element){

        List<WebElement>elements=element.findElements(By.tagName("div"));
        for(WebElement w:elements){
            if(w.getText().equals("Это закрытый аккаунт")){
                System.out.println("Это закрытый аккаунт");
                return false;
            }
        }

        List<WebElement>elemens=element.findElement(By.tagName("div")).findElements(By.className("Dw_ki"));
        List<WebElement>elementList=elemens.get(1).findElements(By.className("_81NM2"));
        int publication = Integer.parseInt(elementList.get(0).getText().replaceAll("\\D",""));//Количество публикаций

        if(publication<1){
            System.out.println("Публикаций меньше " + 1);
            return false;
        }

        return true;

    }

}
