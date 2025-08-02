package org.grano22.dev.githubscanner.core.contract;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.grano22.dev.githubscanner.core.GitRemoteRepositoryDetails;

import java.io.IOException;
import java.util.HashSet;

public class GithubRepositoryResponseDeserializer extends JsonDeserializer<GitRemoteRepositoryDetails> {
    public GitRemoteRepositoryDetails deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        JsonNode node = context.readTree(jsonParser);

        if (!node.isObject()) {
            throw new JsonParseException(jsonParser, "Root structure should be an object");
        }

        String name =  node.get("name").asText();
        boolean isFork = node.get("fork").asBoolean();
        JsonNode owner = node.get("owner");

        if (owner == null || !owner.isObject()) {
            throw new JsonParseException(jsonParser, "owner must be an nested object");
        }

        if (owner.get("login") == null || !owner.get("login").isTextual() || owner.get("login").asText().isBlank()) {
            throw new JsonParseException(jsonParser, "login must be a non-empty string");
        }

        String ownerLogin = owner.get("login").asText();

       return new GitRemoteRepositoryDetails(name, ownerLogin, new HashSet<>(), isFork);
    }
}
