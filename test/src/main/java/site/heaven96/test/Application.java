package site.heaven96.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import site.heaven96.limiter.annotation.EnableLimiter;


@SpringBootApplication
@EnableLimiter
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
