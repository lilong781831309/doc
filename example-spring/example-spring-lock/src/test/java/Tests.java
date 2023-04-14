import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xinhua.example.spring.lock.ExampleApplication;
import org.xinhua.example.spring.lock.config.LockRegistryConfig;
import org.xinhua.example.spring.lock.service.AccessToken;
import org.xinhua.example.spring.lock.service.TokenService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApplication.class)
public class Tests {

    @Autowired
    private LockRegistryConfig lockRegistryConfig;

    @Autowired
    private TokenService tokenService;

    @Test
    public void test() throws Exception {
        String prefix = lockRegistryConfig.getPrefix();
        Long defaultExpireMillis = lockRegistryConfig.getDefaultExpireMillis();

        System.out.println(prefix);
        System.out.println(defaultExpireMillis);
    }

    @Test
    public void test2() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    AccessToken token = tokenService.getToken();
                    System.out.println(Thread.currentThread().getName() + "==token==" + token.getAccess_token());
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    AccessToken token = tokenService.getToken();
                    System.out.println(Thread.currentThread().getName() + "==token==" + token.getAccess_token());
                }
            }
        }, "B").start();

        Thread.sleep(Long.MAX_VALUE);
    }

}
