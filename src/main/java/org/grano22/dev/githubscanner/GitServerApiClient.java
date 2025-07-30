package org.grano22.dev.githubscanner;

import java.util.Set;

public interface GitServerApiClient {
    Set<GitRemoteRepositoryDetails> getRepositories(String ownerName);
}
