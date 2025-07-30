package org.grano22.dev.githubscanner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class GitRemoteBranchDetailsDeserializer extends JsonDeserializer<GitRemoteBranchDetails> {
    @Override
    public GitRemoteBranchDetails deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = context.readTree(jsonParser);

        String branchName = requireAsNonEmptyText(jsonParser, node.get("name"), "name");

        JsonNode lastCommitSummary = node.get("commit");

        if (lastCommitSummary == null || !lastCommitSummary.isObject()) {
            throw new JsonParseException(jsonParser, "commit is required as object");
        }

        String lastCommitSha = requireAsNonEmptyText(jsonParser, lastCommitSummary.get("sha"), "commit.sha");

        return new GitRemoteBranchDetails(branchName,lastCommitSha);
    }

    private String requireAsNonEmptyText(JsonParser jsonParser, JsonNode node, String identifier) throws JsonParseException {
        if (node == null || !node.isTextual() || node.asText().isEmpty()) {
            throw new JsonParseException(jsonParser, identifier + " is required");
        }

        return node.asText();
    }
}
