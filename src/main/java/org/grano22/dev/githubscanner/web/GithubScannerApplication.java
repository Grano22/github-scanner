package org.grano22.dev.githubscanner.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.grano22.dev.githubscanner.core",
    "org.grano22.dev.githubscanner.web"
})
public class GithubScannerApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GithubScannerApplication.class);

        Properties properties = new Properties();
        properties.setProperty("spring.config.location", "classpath:/web/application.properties");
        app.setDefaultProperties(properties);

        app.run(args);
    }

}
