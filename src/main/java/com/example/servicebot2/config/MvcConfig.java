package com.example.servicebot2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {



    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");//setViewName("login")-Указываем тмяшаблону к которому нужно обращаться

    }


}
