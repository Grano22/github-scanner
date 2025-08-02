package org.grano22.dev.githubscanner.core.external;

import org.grano22.dev.githubscanner.core.GitRemoteRepositoryDetails;

import java.util.Set;

public interface GitServerApiClient {
    Set<GitRemoteRepositoryDetails> getRepositories(String ownerName);
}
