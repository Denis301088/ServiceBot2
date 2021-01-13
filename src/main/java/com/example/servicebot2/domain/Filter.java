package com.example.servicebot2.domain;

import com.example.servicebot2.service.WordsFileComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Embeddable
public class Filter {

    private String min_subscribers;

    private String max_subscribers;

    private String min_subscription;

    private String max_subscription;

    private String min_publication;

    private String max_publication;

    private String date_publication;

    private boolean privateAccount;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private boolean avatar;

    private boolean skipOldAccounts;

    private String whiteWords;

    private String stopWords;

    private boolean alienAccount;

    private boolean commercialAccount;

    private String listUsersIgnore;

    @Transient
    private WordsFileComponent wordsFileComponent;


    public Filter(){}

    public String getMin_subscribers() {
        return min_subscribers;
    }

    public void setMin_subscribers(String min_subscribers) {
        this.min_subscribers = min_subscribers;
    }

    public String getMax_subscribers() {
        return max_subscribers;
    }

    public void setMax_subscribers(String max_subscribers) {
        this.max_subscribers = max_subscribers;
    }

    public String getMin_subscription() {
        return min_subscription;
    }

    public void setMin_subscription(String min_subscription) {
        this.min_subscription = min_subscription;
    }

    public String getMax_subscription() {
        return max_subscription;
    }

    public void setMax_subscription(String max_subscription) {
        this.max_subscription = max_subscription;
    }

    public String getMin_publication() {
        return min_publication;
    }

    public void setMin_publication(String min_publication) {
        this.min_publication = min_publication;
    }

    public String getMax_publication() {
        return max_publication;
    }

    public void setMax_publication(String max_publication) {
        this.max_publication = max_publication;
    }

    public String getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(String date_publication) {
        this.date_publication = date_publication;
    }

    public boolean isPrivateAccount() {
        return privateAccount;
    }

    public void setPrivateAccount(boolean privateAccount) {
        this.privateAccount = privateAccount;
    }

    public boolean isAvatar() {
        return avatar;
    }

    public void setAvatar(boolean avatar) {
        this.avatar = avatar;
    }

    public boolean isSkipOldAccounts() {
        return skipOldAccounts;
    }

    public void setSkipOldAccounts(boolean skipOldAccounts) {
        this.skipOldAccounts = skipOldAccounts;
    }

    public String getWhiteWords() {
        return whiteWords;
    }

    public void setWhiteWords(String whiteWords) {
        this.whiteWords = whiteWords;
    }

    public String getStopWords() {
        return stopWords;
    }

    public void setStopWords(String stopWords) {
        this.stopWords = stopWords;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isAlienAccount() {
        return alienAccount;
    }

    public void setAlienAccount(boolean alienAccount) {
        this.alienAccount = alienAccount;
    }

    public boolean isCommercialAccount() {
        return commercialAccount;
    }

    public void setCommercialAccount(boolean commercialAccount) {
        this.commercialAccount = commercialAccount;
    }

    public String getListUsersIgnore() {
        return listUsersIgnore;
    }

    public void setListUsersIgnore(String listUsersIgnore) {
        this.listUsersIgnore = listUsersIgnore;
    }

    public WordsFileComponent getWordsFileComponent() {
        return wordsFileComponent;
    }

    public void setWordsFileComponent(WordsFileComponent wordsFileComponent) {
        this.wordsFileComponent = wordsFileComponent;
    }

    
    public boolean methodFilter_OldAccounts(Task task, String href){

        if(this.skipOldAccounts) return task.getAccount().getMemoryLinksProfiles().contains(href);
        
        return false;
    }


    //==================================================  Первичный фильтр  =============================================================

    private boolean FirstMethodFilter_privateAccount(WebElement element){

        if(this.privateAccount){
            List<WebElement>elements=element.findElements(By.tagName("div"));
            for(WebElement w:elements){
                if(w.getText().equals("Это закрытый аккаунт")){
                    System.out.println("Это закрытый аккаунт");
                    return false;
                }
            }
        }
        return true;
    }


    private boolean FirstMethodFilter_avatar(WebElement element){

        if(this.avatar){
            String avatar=element.findElement(By.tagName("div")).findElement(By.tagName("a")).findElement(By.tagName("img")).getAttribute("src");////*[@id="react-root"]/section/main/div/header/div/div/div/button/img   //*[@id="react-root"]/section/main/div/header/div/div/span/img

            boolean b=!avatar.matches(".*(YW5vbnltb3VzX3Byb2ZpbGVfcGlj.2)");
            if(!b){
                System.out.println("Не прошло  по фильтру: АВАТАР ");
            }
            return b;//YW5vbnltb3VzX3Byb2ZpbGVfcGlj.2
        }
        return true;

    }


    public boolean FirstMethodFilter_Gender(WebElement element){

        if(this.gender == Gender.MAN){//"[^A-Za-zА-Яа-я&&[^ ]]",""
            try{

                String[]name=element.findElement(By.tagName("div")).findElement(By.className("_4BSuu")).getText().replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");

                for (String s:name){
                    if(!s.isEmpty() && wordsFileComponent.getManNames().contains(s))return true;//s.replaceAll("[^A-Za-zА-Яа-я&&[^ ]]","").toLowerCase()
                }
            }catch (Exception ex){

            }
            return false;

        }else if(this.gender==Gender.WOMEN){
            try{
                String ss=element.findElement(By.tagName("div")).findElement(By.className("_4BSuu")).getText();
                if(ss.equals("\uD83D\uDE07АngeLina\uD83D\uDE07")){
                    System.out.println(ss);
                }
                String[]name=element.findElement(By.tagName("div")).findElement(By.className("_4BSuu")).getText().replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");
                for (String s:name){

                    if(!s.isEmpty() && wordsFileComponent.getWomenNames().contains(s)){
                        System.out.println("Прошло по фильтру ЖЕНЩИНЫ");
                        return true;
                    }
                }
            }catch (Exception ex){

            }
            System.out.println("НЕПрошло по фильтру ЖЕНЩИНЫ");
            return false;

        }
        return true;

    }


    private boolean FirstMethodFilter_publication(WebElement element){

        if(!StringUtils.isEmpty(this.min_publication) || !StringUtils.isEmpty(this.max_publication)){

            List<WebElement>elemens=element.findElement(By.tagName("div")).findElements(By.className("Dw_ki"));
            List<WebElement>elementList=elemens.get(1).findElements(By.className("_81NM2"));
            int publication = Integer.parseInt(elementList.get(0).getText().replaceAll("\\D",""));//Количество публикаций

            if(!StringUtils.isEmpty(min_publication)){
                if(Integer.parseInt(min_publication)>publication){
                    System.out.println("Публикаций меньше " +Integer.parseInt(min_publication));
                    return false;
                }
            }
            if(!StringUtils.isEmpty(max_publication)){
                if(Integer.parseInt(max_publication)<publication){
                    System.out.println("Публикаций больше " +Integer.parseInt(max_publication));
                    return false;
                }
            }


        }
        return true;

    }


    private boolean FirstMethodFilter_subscribers(WebElement element){
        if(!StringUtils.isEmpty(this.min_subscribers) || !StringUtils.isEmpty(this.max_subscribers)){

            List<WebElement>elemens=element.findElement(By.tagName("div")).findElements(By.className("Dw_ki"));
            List<WebElement>elementList=elemens.get(1).findElements(By.className("_81NM2"));
            int subscrib = Integer.parseInt(elementList.get(1).getAttribute("innerText").replaceAll("\\D",""));
            System.out.println(subscrib);

            if(!StringUtils.isEmpty(min_subscribers)){
                if(Integer.parseInt(min_subscribers)>subscrib){
                    System.out.println("Подписчиков меньше " +Integer.parseInt(min_subscribers));
                    return false;
                }
            }
            if(!StringUtils.isEmpty(max_subscribers)){
                if(Integer.parseInt(max_subscribers)<subscrib){
                    System.out.println("Подписчиков больше " +Integer.parseInt(max_subscribers));
                    return false;
                }
            }

        }
        return true;
    }


    private boolean FirstMethodFilter_subscription(WebElement element) {

        if (!StringUtils.isEmpty(this.min_subscription) || !StringUtils.isEmpty(this.max_subscription)) {

            List<WebElement> elemens = element.findElement(By.tagName("div")).findElements(By.className("Dw_ki"));
            List<WebElement> elementList = elemens.get(1).findElements(By.className("_81NM2"));
            int subrscrip = Integer.parseInt(elementList.get(2).getText().replaceAll("\\D", ""));
            System.out.println(subrscrip);

            if (!StringUtils.isEmpty(min_subscription)) {
                if (Integer.parseInt(min_subscription) > subrscrip){
                    System.out.println("Подписок меньше " +Integer.parseInt(min_subscription));
                    return false;
                }
            }
            if (!StringUtils.isEmpty(max_subscription)) {
                if (Integer.parseInt(max_subscription) < subrscrip){
                    System.out.println("Подписок больше " +Integer.parseInt(max_subscription));
                    return false;
                }
            }

        }
        return true;

    }


    private boolean FirstMethodFilterDate_publication(ChromeDriver driver,WebElement element){


        if(!StringUtils.isEmpty(this.date_publication)){


            List<WebElement>elements=element.findElements(By.tagName("div"));

            for(WebElement w:elements){
                if(w.getText().equals("Это закрытый аккаунт"))return true;
            }

            List<WebElement>elemens1=element.findElement(By.tagName("div")).findElements(By.className("Dw_ki"));
            List<WebElement>elementList=elemens1.get(1).findElements(By.className("_81NM2"));
            int lastpublication = Integer.parseInt(elementList.get(0).getText().replaceAll("\\D",""));//Количество публикаций

            if(lastpublication==0){ return false; }
            else {

                List<WebElement>elements1=element.findElement(By.tagName("div")).findElements(By.className("ZwOlu"));
                elements1.get(0).click();//Захожу в последнюю публикацию

                String s=driver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/div[1]/article/div[3]/div[2]/a/time")).getAttribute("datetime");
                s=s.substring(0,s.indexOf("T"));
                LocalDate localDate=LocalDate.now();
                LocalDate localDate1=LocalDate.parse(s);
                long days = localDate.toEpochDay()-localDate1.toEpochDay();

                if(Integer.parseInt(date_publication) < days){
                    System.out.println(days+" больше " +date_publication);
                    driver.navigate().back();
                    return false;}

                driver.navigate().back();

            }

        }

        return true;
    }


    //================================================================================================================


    public boolean methodFilter_AlienAccount(ChromeDriver driver){

        if(this.alienAccount){
            try {
                String text=driver.findElement(By.className("-vDIg")).getText();//.findElement(By.tagName("span"))
                boolean b=text.matches(".*[А-Яа-я\n].*");
                if(!b){
                    System.out.println("Не прошло по фильтру: АККАУНТЫ ИНОСТРАНЦЕВ");
                }
                return b;
            }catch (Exception ex){

            }
            System.out.println("Не прошло по фильтру: АККАУНТЫ ИНОСТРАНЦЕВ");
            return false;
        }
        return true;
    }

    public boolean methodFilter_CommercialAccount(ChromeDriver driver){

        if(this.commercialAccount){

            try{
                String text=driver.findElement(By.className("-vDIg")).getText();//Получаю весь текст для строших сравнений
//                String []words=text.replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");
                List<String> words=Arrays.stream(text.replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ")).collect(Collectors.toList());

                for(String s:this.wordsFileComponent.getStrictCommercialWords()){
                    if (s.length()<4){
                        if(words.contains(s)){
                            System.out.println("Не прошло  СТРОГИХ по слову "+ s);
                            return false;
                        }
                        continue;
                    }
                    if(text.contains(s)){

                        System.out.println("Не прошло  СТРОГИХ по слову "+ s);
                        return false;
                    }

                }

//                String []words=text.replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");
                for (String s:words){

                    if(!s.isEmpty() && s.length()>2) if(wordsFileComponent.getNonStrictCommercialWords().contains(s)){

                        System.out.println("Не прошло  НЕСТРОГИХ по слову "+ s);
                        return false;
                    }

                }

            }catch (Exception ex){

            }

            return true;
        }
        return true;

    }


    private boolean methodFilter_whiteWords(ChromeDriver driver){

        if(!StringUtils.isEmpty(whiteWords)){

            try{

                String []words=whiteWords.toLowerCase().split("\r\n");
                String[]text=driver.findElement(By.className("-vDIg")).getText().replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");
                for(String s:text){
                    for (String s1:words){
                        if(s.equals(s1.trim()))return true;
                    }
                }

            }catch (Exception ex){
                return false;//можно убрать
            }

            return false;
        }
        return true;
    }


    private boolean methodFilter_stopWords(ChromeDriver driver){

        if(!StringUtils.isEmpty(stopWords)){

            try{

                String []words=stopWords.toLowerCase().split("\r\n");
                String[]text=driver.findElement(By.className("-vDIg")).getText().replaceAll("[^A-Za-zА-Яа-я&&[^ ]]"," ").toLowerCase().split(" ");
                for(String s:text){
                    for (String s1:words){
                        if(s.equals(s1.trim()))return false;
                    }
                }

            }catch (Exception ex){
                return true;//можно убрать
            }

            return true;
        }
        return true;
    }


    public boolean firstFilterAccounts(ChromeDriver driver,WebElement element){

        if(FirstMethodFilter_privateAccount(element)
                && FirstMethodFilter_avatar(element)
                && FirstMethodFilter_Gender(element)
                && FirstMethodFilter_publication(element)
                && FirstMethodFilter_subscribers(element)
                && FirstMethodFilter_subscription(element)
                && FirstMethodFilterDate_publication(driver,element)
        ) return true;
        else return false;
    }


    public boolean filterAccounts(ChromeDriver driver){

        if(methodFilter_AlienAccount(driver)
                && methodFilter_whiteWords(driver)
                && methodFilter_stopWords(driver)
                && methodFilter_CommercialAccount(driver)) return true;
        else return false;

    }

    public List<String>usersIgnore(Task task){
        List<String>usersIgnore=new ArrayList<>();
        if(!task.getFilter().getListUsersIgnore().isEmpty()){
            for(String s:task.getFilter().getListUsersIgnore().split("\r\n")){
                usersIgnore.add("https://www.instagram.com/"+s.trim()+"/");
            }
        }
        return usersIgnore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filter filter = (Filter) o;
        return avatar == filter.avatar &&
                alienAccount == filter.alienAccount &&
                commercialAccount == filter.commercialAccount &&
                Objects.equals(min_subscribers, filter.min_subscribers) &&
                Objects.equals(max_subscribers, filter.max_subscribers) &&
                Objects.equals(min_subscription, filter.min_subscription) &&
                Objects.equals(max_subscription, filter.max_subscription) &&
                Objects.equals(min_publication, filter.min_publication) &&
                Objects.equals(max_publication, filter.max_publication) &&
                Objects.equals(date_publication, filter.date_publication) &&
                gender == filter.gender &&
                Objects.equals(whiteWords, filter.whiteWords) &&
                Objects.equals(stopWords, filter.stopWords);
    }

}
