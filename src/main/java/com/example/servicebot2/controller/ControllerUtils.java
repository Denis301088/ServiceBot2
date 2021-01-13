package com.example.servicebot2.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ControllerUtils {//Выносим метод обработки валидации в отдельный класс

    static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError,?,Map<String,String>> collector= Collectors.toMap(fieldError->fieldError.getField()+"Error",fieldError->fieldError.getDefaultMessage());
        return bindingResult.getFieldErrors().stream().collect(collector);
    }
}
