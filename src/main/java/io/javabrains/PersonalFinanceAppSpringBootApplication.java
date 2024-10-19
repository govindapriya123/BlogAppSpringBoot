package io.javabrains;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages={"io.javabrains.Controllers","io.javabrains.Services"})
public class PersonalFinanceAppSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalFinanceAppSpringBootApplication.class, args);
    }

}