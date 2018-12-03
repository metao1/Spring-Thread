package de.vispiron.carsync.thread;

import de.vispiron.carsync.thread.future.ServiceView;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    public ServiceView<Person> peopleServiceView(){
        return new ServiceViewImp<>();
    }
}
