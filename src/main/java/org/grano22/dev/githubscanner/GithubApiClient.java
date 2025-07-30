package org.grano22.dev.githubscanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GithubApiClient implements GitServerApiClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final ObjectMapper githubResponseMapper;

    public GithubApiClient(
        RestTemplateBuilder builder,
        @Value("${github.api.base-url}") String baseUrl,
        @Value("${github.token:}") String token
    ) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;

        if (StringUtils.hasText(token)) {
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "token " + token);
                return execution.execute(request, body);
            });
        }

        SimpleModule module = new SimpleModule();
        module.addDeserializer(GitRemoteRepositoryDetails.class, new GithubRepositoryResponseDeserializer());
        module.addDeserializer(GitRemoteBranchDetails.class, new GitHubBranchResponseDeserializer());

        this.githubResponseMapper = new ObjectMapper();
        this.githubResponseMapper.registerModule(module);
    }

    /** @throws org.grano22.dev.githubscanner.ApiException Generic API exception */
    @Override
    public @Nonnull Set<GitRemoteRepositoryDetails> getRepositories(@Nonnull String ownerName) {
        // TODO: Implement limit constraint
        String endpoint = String.format("%s/users/%s/repos", baseUrl, ownerName);

        try {
            ResponseEntity<String> rawResponse = restTemplate.getForEntity(endpoint, String.class);

            if (!rawResponse.getStatusCode().is2xxSuccessful()) {
                throw new ApiException("Request failed", rawResponse.getStatusCode().value());
            }

            GitRemoteRepositoryDetails[] userRepos = githubResponseMapper.readValue(
                rawResponse.getBody(),
                new TypeReference<>() {}
            );

            if (userRepos == null) {
                throw new ApiException("Unable to deserialize github repositories response", -1);
            }

            return Arrays.stream(userRepos)
                    .filter(repo -> !repo.isFork())
                    .map(repo -> new GitRemoteRepositoryDetails(
                            repo.name(),
                            repo.ownerLogin(),
                            getBranches(ownerName, repo.name()),
                            false
                    ))
                    .collect(Collectors.toSet())
            ;
        } catch (RestClientException|JsonProcessingException e) {
            throw new ApiException(e.getMessage(), -1);
        }
    }

    /** @throws org.grano22.dev.githubscanner.ApiException Generic API exception */
    private @Nonnull Set<GitRemoteBranchDetails> getBranches(@Nonnull String ownerName, @Nonnull String repoName) {
        String endpoint = String.format("%s/repos/%s/%s/branches", baseUrl, ownerName, repoName);

        try {
            ResponseEntity<String> rawResponse = restTemplate.getForEntity(endpoint, String.class);

            if (!rawResponse.getStatusCode().is2xxSuccessful()) {
                throw new ApiException("Request failed", rawResponse.getStatusCode().value());
            }

            GitRemoteBranchDetails[] reposBranches = githubResponseMapper.readValue(
                 rawResponse.getBody(),
                 new TypeReference<>() {}
            );

            if (reposBranches == null) {
                throw new ApiException("Unable to deserialize github branches response", -1);
            }

            return Set.of(reposBranches);
        } catch (RestClientException|IllegalArgumentException|JsonProcessingException e) {
            throw new ApiException(e.getMessage(), -1);
        }
    }
}
