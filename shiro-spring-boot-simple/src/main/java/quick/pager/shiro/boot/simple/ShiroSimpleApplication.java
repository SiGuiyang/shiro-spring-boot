package quick.pager.shiro.boot.simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import quick.pager.shiro.spring.boot.annotation.EnableShiroConfiguration;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableShiroConfiguration
public class ShiroSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroSimpleApplication.class, args);
    }

}
