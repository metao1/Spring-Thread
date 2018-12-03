package com.metao.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    public ServiceView<Person> peopleServiceView(){
        return new ServiceViewImp<>();
    }
}
