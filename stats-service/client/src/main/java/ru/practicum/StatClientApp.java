package ru.practicum;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@OpenAPIDefinition(info = @Info(title = "ExploreWithMe", version = "1.0", description = "ShareIt app swagger"))
public class StatClientApp {

    public static void main(String[] args) {
        SpringApplication.run(StatClientApp.class, args);
    }

}
