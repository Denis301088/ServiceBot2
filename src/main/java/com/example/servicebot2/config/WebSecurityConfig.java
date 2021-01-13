package com.example.servicebot2.config;

import com.example.servicebot2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(8);//в параметре надежность ключа шифрования
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/registration","/activate/*","/newpass","/newpass/*").permitAll()//permitAll()дает полный доступ к странице на котррую приходит пользователь по указанному пути.Для остальных запростов будет требоваться авторизация
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")//Подключаем форму для заполнения логина
                .defaultSuccessUrl("/history").failureUrl("/login")//которая будет лежать по этому пути,при удачном входе переводит на страницу /history
                .permitAll()//Даем разрешение пользоваться этой формой всем
                .and()
                .rememberMe()//включаем сохранение параметров запроса при сбросе сессии.Если есть несколько серверов или сброс сесии происходит при перезапуске приложения,то идентификационные данные лучше хранить на внешнем кэше,например redis.В данном случае проскольку кейс простой,будем хранить данные в базе
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/")//Подключаем форму выхода
                .permitAll();//даем разрешеие пользоваться формой выхода всем
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);

//        auth.jdbcAuthentication().dataSource(dataSource).//Передаем в метод dataSource,котороая автоматически будет генерироваться спрингом,чтобы менеджер мог ходить в базу данных и получать пользователей
//        passwordEncoder(NoOpPasswordEncoder.getInstance()).//passwordEncoder шифрует пароли,чтобы они не хранились в явном виде
//        usersByUsernameQuery("select username, password, active from usr where username=?").//Поля нужно указывать именно в такой последовательности,т к набор полей определен системой
//        authoritiesByUsernameQuery("select u.username, ur.roles from usr u inner join user_role ur on u.id=ur.user_id where u.username=?");

    }
}
