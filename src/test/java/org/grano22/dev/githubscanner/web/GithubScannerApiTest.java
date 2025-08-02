package org.grano22.dev.githubscanner.web;

import org.grano22.dev.githubscanner.core.GitRemoteBranchDetails;
import org.grano22.dev.githubscanner.core.GitRemoteRepositoryDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubScannerApiTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    // I prefer more RestAssured, but for simplicity it can be TestRestTemplate

    @Test
    public void githubRepositoriesWereFetchedSuccessfullyForAGivenOwnerName() {
        // Given
        String username = "Blaszak";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ParameterizedTypeReference<Set<GitRemoteRepositoryDetails>> responseType = new ParameterizedTypeReference<>() {};

        // When
        ResponseEntity<Set<GitRemoteRepositoryDetails>> response = testRestTemplate.exchange(
            "/api/v1/user/{ownerLogin}/repos",
            HttpMethod.GET,
            request,
            responseType,
            username
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Set<GitRemoteRepositoryDetails> repositories = response.getBody();

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
