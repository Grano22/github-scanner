package org.grano22.dev.githubscanner.cli;


import org.grano22.dev.githubscanner.core.contract.FailedToProvideApiSpec;
import org.grano22.dev.githubscanner.core.contract.GithubApiSpecProvider;
import org.grano22.dev.githubscanner.core.infrastructure.DownloadFailed;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class CliUpdateSpecCommandTest {
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @Autowired
    private CommandLine.IFactory factory;

    @Autowired
    private MainCommand mainCommand;

    @MockitoBean
    private GithubApiSpecProvider specProvider;

    @BeforeEach
    void setUp() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void updateSpecCommandHasCorrectAutoSetup() throws FailedToProvideApiSpec {
        CommandLine cli = new CommandLine(mainCommand, factory);
        Mockito.when(specProvider.downloadAndGetLatestSpec()).thenReturn("spec_v1");

        int exitCode = cli.execute("update-github-api-spec", "-v");

        assertSame(0, exitCode);
        assertEquals("Github api spec updated successfully\r\n", outContent.toString());
    }

    @Test
    void updateSpecCommandCorrectlyHandlesServiceOutage() throws FailedToProvideApiSpec {
        CommandLine cli = new CommandLine(mainCommand, factory);
        Mockito.when(specProvider.downloadAndGetLatestSpec())
            .thenThrow(
                FailedToProvideApiSpec.dueToDownloadFailure(new DownloadFailed("Failed to download a spec", null))
            )
        ;

        int exitCode = cli.execute("update-github-api-spec", "-v");

        assertSame(0, exitCode);
        assertEquals("Failed to update the github api spec due to: Source API was unavailable\r\n", outContent.toString());
    }
}
