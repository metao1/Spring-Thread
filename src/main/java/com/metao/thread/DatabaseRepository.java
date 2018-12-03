package com.metao.thread;

import org.springframework.stereotype.Repository;

@Repository
public class DatabaseRepository {

    Person[] people = {new Person("Ali"), new Person("Mohammad"), new Person("Reza")};

    public Person[] getPeople() {
        return people;
    }

}
