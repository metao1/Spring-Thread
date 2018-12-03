package de.vispiron.carsync.thread.test;

import de.vispiron.carsync.thread.future.Job;
import de.vispiron.carsync.thread.future.JobExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobModelExecutorTest {

    @Autowired
    JobExecutor jobExecutor;

    private CountDownLatch lock = new CountDownLatch(4);

    @Test
    public void testRunner() throws InterruptedException {
        jobExecutor.startJobs(this);
        lock.await(1, TimeUnit.MINUTES);
    }

    @Before
    public void beforeTest() {

    }

    @Job(name = "test job1", priority = 1)
    public void run1() {
        try {
            System.out.println("Thread1 is running...");
            lock.countDown();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Job(name = "test job", priority = 2)
    public void run2() {
        try {
            System.out.println("Thread2 is running...");
            lock.countDown();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Job(name = "test job", priority = 3)
    public void run3() {
        try {
            System.out.println("Thread3 is running...");
            lock.countDown();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Job(name = "test job", priority = 4)
    public void run4() {
        try {
            System.out.println("Thread4 is running...");
            lock.countDown();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
