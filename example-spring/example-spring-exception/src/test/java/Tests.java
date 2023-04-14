import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.xinhua.example.spring.exception.ExampleApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExampleApplication.class)
public class Tests {

    @Test
    public void test() throws Exception {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("i18n/messages");

        Object[] params = {"张"};
        //String s = messageSource.getMessage("10001", params, "aaa", Locale.CHINA);
        String s = messageSource.getMessage("10001", params, "aaa", LocaleContextHolder.getLocale());

        System.out.println(s);
    }

}
