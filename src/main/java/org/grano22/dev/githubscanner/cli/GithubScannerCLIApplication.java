package org.grano22.dev.githubscanner.cli;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import picocli.CommandLine;

import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.grano22.dev.githubscanner.core",
    "org.grano22.dev.githubscanner.cli"
})
public class GithubScannerCLIApplication implements CommandLineRunner, ExitCodeGenerator {
    private int exitCode;
    private final CommandLine commandLine;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GithubScannerCLIApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.setLogStartupInfo(false);

        Properties properties = new Properties();
        properties.setProperty("spring.config.location", "classpath:/cli/application.properties");
        app.setDefaultProperties(properties);

        ConfigurableApplicationContext context = app.run(args);

        System.exit(SpringApplication.exit(context));
    }

    public GithubScannerCLIApplication(CommandLine commandLine) {
        this.commandLine = commandLine;
    }

    @Override
    public void run(String... args) throws Exception {
        exitCode = commandLine.execute(args);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
