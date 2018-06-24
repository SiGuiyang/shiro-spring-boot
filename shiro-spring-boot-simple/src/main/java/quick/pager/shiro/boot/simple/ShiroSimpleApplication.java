package quick.pager.shiro.boot.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import quick.pager.shiro.spring.boot.annotation.EnableShiroConfiguration;

@SpringBootApplication
@EnableShiroConfiguration
public class ShiroSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroSimpleApplication.class, args);
    }

}
