package de.vispiron.carsync.thread.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@RunWith(JUnit4.class)
public class QueueTest {


    @Test
    public void testQueue() {
        BlockingDeque<Integer> q = new LinkedBlockingDeque<>();

        Random r = new Random();

        System.out.print("Input: ");
        for (int i = 0; i < 100; i++) {
            int x = r.nextInt(1000);
            System.out.print(x + " ");
            q.offer(x);
        }

        System.out.println();
        System.out.print("Output: ");

        while (q.size() > 0)
            System.out.print(q.poll() + " ");
    }
}
