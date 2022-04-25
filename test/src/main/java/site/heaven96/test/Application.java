package site.heaven96.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.heaven96.limiter.annotation.EnableLimiter;
import site.heaven96.log.handle.impl.DefaultLogHandle;

import javax.annotation.Resource;


@SpringBootApplication
@EnableLimiter
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
