package org.grano22.dev.githubscanner.cli.kit;

import picocli.CommandLine;

@CommandLine.Command(name = "dummy")
public class DummyCommand implements Runnable {
    @Override
    public void run() {
    }
}
