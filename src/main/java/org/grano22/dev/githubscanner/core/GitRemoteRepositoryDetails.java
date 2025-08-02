package org.grano22.dev.githubscanner.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record GitRemoteRepositoryDetails(
     String name,
     String ownerLogin,
     Set<GitRemoteBranchDetails> branches,
     @JsonProperty("fork") boolean isFork
) {
}
