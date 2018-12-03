package de.vispiron.carsync.thread.test;

import de.vispiron.carsync.thread.future.Utils;
import de.vispiron.carsync.thread.future.ExecutorsHelper;
import de.vispiron.carsync.thread.future.PriorityExecutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author karamim
 */

@RunWith(SpringRunner.class)
public class JobExecutorTest {

    private CountDownLatch lock = new CountDownLatch(12);

    @Test
    public void testJobExecutor() throws InterruptedException {
        PriorityExecutorService service = ExecutorsHelper.newPriorityFixedThreadPool(11);
        for (int i = 1; i < 11; i++) {
            int finalI = i;
            service.submit(() -> System.out.println(Utils.format("run thread", String.valueOf(finalI))),i);
            lock.countDown();
        }

        lock.await(1, TimeUnit.MINUTES);
    }

    @Test
    public void testJobExecutorWithResult() throws InterruptedException {
        PriorityExecutorService service = ExecutorsHelper.newPriorityFixedThreadPool(11);
        for (int i = 1; i < 11; i++) {
            int finalI = i;
            final Future<String> thread = service.submit(() -> System.out.println(Utils.format("run thread", String.valueOf(finalI)))
                    , new String("11")
                    , i);
            lock.countDown();
        }

        lock.await(1, TimeUnit.MINUTES);
    }

}
