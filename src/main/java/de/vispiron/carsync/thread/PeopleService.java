package de.vispiron.carsync.thread;

import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PeopleService {

    @Autowired
    DatabaseRepository databaseRepository;


    public Flowable<Person> getPeopleObserver() {
        return Flowable.fromArray(databaseRepository.getPeople());
    }
}
