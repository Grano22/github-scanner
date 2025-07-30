package org.grano22.dev.githubscanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/")
public class GithubScannerController {
    private final GithubApiClient githubApiClient;

    public GithubScannerController(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @GetMapping("/user/{ownerLogin}/repos")
    public ResponseEntity<?> getRepositories(@PathVariable String ownerLogin) {
        try {
            return ResponseEntity.ok(githubApiClient.getRepositories(ownerLogin));
        } catch (ApiException e) {
            if (e.getCode() == 404) {
                Map<String, Object> errorBody = new HashMap<>();
                errorBody.put("status", HttpStatus.NOT_FOUND.value());
                errorBody.put("message",e.getMessage());

                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body(errorBody)
                ;
            }

            return ResponseEntity.status(e.getCode()).build();
        }
    }
}
