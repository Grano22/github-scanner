package org.grano22.dev.githubscanner.cli.config;

import org.grano22.dev.githubscanner.cli.MainCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import picocli.CommandLine;

@Configuration
public class PicocliConfig {
    @Bean
    public CommandLine commandLine(MainCommand mainCommand, CommandLine.IFactory factory) {
        return new CommandLine(mainCommand, factory);
    }
}
