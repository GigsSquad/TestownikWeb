package pl.wropol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableTransactionManagement
public class WroPolBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(WroPolBootApplication.class, args);
    }
}
