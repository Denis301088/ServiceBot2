package com.example.servicebot2.service;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@Component
@Scope("singleton")
public class WordsFileComponent {

    private Set<String> strictCommercialWords=new HashSet<>();
    private Set<String> nonstrictCommercialWords=new HashSet<>();

    private Set<String> womenNames=new HashSet<>();

    private Set<String> manNames=new HashSet<>();

    @PostConstruct
    public void doFileInitialization(){

        try(Scanner scanner=new Scanner(new FileInputStream("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\strict_commercial_words.txt"));//
            Scanner scanner1=new Scanner(new FileInputStream("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\women_names.txt"));
            Scanner scanner2=new Scanner(new FileInputStream("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\man_names.txt"));
            Scanner scanner3=new Scanner(new FileInputStream("C:\\Intellij_Idea__Practicum\\ServiceBot2\\src\\main\\resources\\nonstrict_commercial_words.txt"))){

            while (scanner.hasNext()){
                strictCommercialWords.add(scanner.nextLine());
            }
            while (scanner3.hasNext()){
                nonstrictCommercialWords.add(scanner3.nextLine());
            }
            while (scanner1.hasNext()){
                womenNames.add(scanner1.nextLine());
            }
            while (scanner2.hasNext()){
                manNames.add(scanner2.nextLine());
            }




        }catch (Exception ex){
            ex=new Exception("Ошибка работы файлов со списками ключевых слов");
            ex.printStackTrace();
        }


    }

    public Set<String> getStrictCommercialWords() {
        return strictCommercialWords;
    }

    public Set<String> getNonStrictCommercialWords() {
        return nonstrictCommercialWords;
    }

    public Set<String> getWomenNames() {
        return womenNames;
    }

    public Set<String> getManNames() {
        return manNames;
    }
}
