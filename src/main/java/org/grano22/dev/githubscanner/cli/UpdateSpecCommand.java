package org.grano22.dev.githubscanner.cli;

import org.grano22.dev.githubscanner.core.contract.FailedToProvideApiSpec;
import org.grano22.dev.githubscanner.core.contract.GithubApiSpecProvider;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Component
@Command(name = "update-github-api-spec", description = "Use this command to update the github api spec")
public class UpdateSpecCommand implements Runnable {
    @CommandLine.ArgGroup
    CommonOptions commonOptions;

    private final GithubApiSpecProvider specProvider;

    public UpdateSpecCommand(GithubApiSpecProvider specProvider) {
        this.specProvider = specProvider;
    }

    @Override
    public void run() {
        try {
            specProvider.downloadAndGetLatestSpec();

            System.out.println("Github api spec updated successfully");
        } catch (FailedToProvideApiSpec e) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("Failed to update the github api spec");

            if (commonOptions.verbose) {
                errorMessage.append(" due to: ").append(e.getMessage());
            }

            System.out.println(errorMessage);
        }
    }
}
