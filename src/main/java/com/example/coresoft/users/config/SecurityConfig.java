package com.example.coresoft.users.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()  // Отключение CSRF, если это не требуется
                .authorizeRequests()
                .anyRequest().permitAll() // Разрешить доступ ко всем запросам без аутентификации
                .and()
                .httpBasic(); // Или другие механизмы аутентификации, если это нужно
    }
}
