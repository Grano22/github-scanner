package org.grano22.dev.githubscanner.cli;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.List;

@Component
@CommandLine.Command(
     name = "github-scanner",
     mixinStandardHelpOptions = true,
     subcommands = {
        UpdateSpecCommand.class
    }
)
public class MainCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        System.out.println("No arguments provided. Showing help...");
        spec.commandLine().usage(System.out);
    }
}
