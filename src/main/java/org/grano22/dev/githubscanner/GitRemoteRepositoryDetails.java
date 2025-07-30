package org.grano22.dev.githubscanner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Set;

@JsonDeserialize(using = GItRemoteRepositoryDetailsDeserializer.class)
public record GitRemoteRepositoryDetails(
     String name,
     String ownerLogin,
     Set<GitRemoteBranchDetails> branches,
     boolean isFork
) {
}
