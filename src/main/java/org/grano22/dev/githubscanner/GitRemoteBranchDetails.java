package org.grano22.dev.githubscanner;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = GitRemoteBranchDetailsDeserializer.class)
public record GitRemoteBranchDetails(String name, String lastCommitSha) {
}
