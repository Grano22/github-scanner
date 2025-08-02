package org.grano22.dev.githubscanner.cli;

import picocli.CommandLine;

public class CommonOptions {
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Enable verbose output", scope = CommandLine.ScopeType.LOCAL)
    public boolean verbose;

//    public boolean isVerbosityEnabled() {
//        return verbose;
//    }
}
