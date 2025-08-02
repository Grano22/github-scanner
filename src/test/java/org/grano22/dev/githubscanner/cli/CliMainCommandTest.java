package org.grano22.dev.githubscanner.cli;


import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class CliMainCommandTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Autowired
    private MainCommand mainCommand;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void allCommandsAreListedAndAvailable() {
        CommandLine cli = new CommandLine(mainCommand);

        int exitCode = cli.execute();

        assertSame(0, exitCode);
        assertThat(outContent.toString(), CoreMatchers.containsString("No arguments provided. Showing help...\r\n"));
    }
}
