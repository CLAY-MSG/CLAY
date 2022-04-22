package xyz.sgmi.clay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author MSG
 */
@SpringBootApplication
public class ClayApplication {
    public static void main(String[] args) {
        System.setProperty("apollo.config-service", "http://120.24.68.134:17000");
        SpringApplication.run(ClayApplication.class,args);
    }
}
