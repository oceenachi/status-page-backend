package com.statuspage.status;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@EnableScheduling
@SpringBootApplication
public class StatusApplication {

    @Bean
    BCryptPasswordEncoder BCryptPasswordEncoder(){return new BCryptPasswordEncoder();}


    @Bean
    ModelMapper ModelMapper(){
        return new ModelMapper();
    }

    @Bean
    RestTemplate RestTemplate(RestTemplateBuilder builder){
        return builder
//                .setConnectTimeout(Duration.ofMillis(5000))
//                .setReadTimeout(Duration.ofMillis(10000))
                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(StatusApplication.class, args);
    }


}
