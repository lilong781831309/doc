import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.test.context.junit4.SpringRunner;
import org.xinhua.example.spring.lock.ExampleApplication;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApplication.class)
public class LockTests {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private LockRegistry lockRegistry;

    @Test
    public void rLock() throws Exception {
        long s = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(300);

        for (int i = 0; i < 300; i++) {
            final int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RLock lock = redissonClient.getLock("rLock:" + n);

                    for (int j = 0; j < 10; j++) {
                        try {
                            lock.tryLock(1, TimeUnit.SECONDS);
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            lock.unlock();
                        }
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        long e = System.currentTimeMillis();
        System.out.println("cost time : " + (e - s));
    }

    @Test
    public void lockRegistry() throws Exception {
        long s = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(300);

        for (int i = 0; i < 300; i++) {
            final int n = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Lock lock = lockRegistry.obtain("lock:" + n);
                    for (int j = 0; j < 10; j++) {
                        try {
                            lock.tryLock(1, TimeUnit.SECONDS);
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        } finally {
                            lock.unlock();
                        }
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }

        countDownLatch.await();
        long e = System.currentTimeMillis();
        System.out.println("cost time : " + (e - s));
    }

}
