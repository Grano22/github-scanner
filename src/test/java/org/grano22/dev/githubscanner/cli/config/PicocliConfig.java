package org.grano22.dev.githubscanner.cli.config;

import org.grano22.dev.githubscanner.cli.kit.DummyCommand;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import picocli.CommandLine;

@TestConfiguration
public class PicocliConfig {
    @Bean
    public CommandLine commandLine(CommandLine.IFactory factory) {
        return new CommandLine(new DummyCommand(), factory);
    }
}
