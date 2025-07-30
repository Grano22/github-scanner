package org.grano22.dev.githubscanner;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class GithubApiClientTest {
    @Autowired
    private GithubApiClient githubApiClient;

    @Test
    public void allNonForkedReposWereListedCorrectlyForParticularUser() {
        // Given
        String username = "Blaszak"; // "Grano22" - beware, quite huge repos amount, rate limit quote can be exceeded immediately

        // When
        Set<GitRemoteRepositoryDetails> repositories = assertDoesNotThrow(() -> githubApiClient.getRepositories(username));

        // Then
        assertThat(repositories)
            .isNotNull()
            .isNotEmpty()
        ;

        repositories
            .forEach(entry -> {
                assertFalse(entry.isFork());
                assertThat(entry.ownerLogin()).isEqualTo(username);
                assertThat(entry.name()).isNotEmpty();
                assertThat(entry.branches()).isNotEmpty();

                GitRemoteBranchDetails firstBranch = entry.branches().iterator().next();

                assertThat(firstBranch.lastCommitSha()).matches("^[a-fA-F0-9]{40}$");
                assertThat(firstBranch.name()).isNotEmpty();
            })
        ;
    }
}
