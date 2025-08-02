package org.grano22.dev.githubscanner.core.contract;

import org.grano22.dev.githubscanner.infrastructure.DownloadFailed;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractMap;

import static org.grano22.dev.githubscanner.infrastructure.ChecksumCalculator.calculateChecksum;

@Component
public class GithubApiSpecProvider {
    private static final String SPEC_URL = "https://raw.githubusercontent.com/github/rest-api-description/main/descriptions/api.github.com/api.github.com.yaml";
    private static final Path TARGET_PATH = Paths.get("src/main/resources/github-openapi.yaml");
    private static final Path CHECKSUM_PATH = Paths.get("src/main/resources/github-openapi.md5");
    private static final String CHECKSUM_ALGORITHM = "MD5";

    public enum SpecStatus {
        FRESH,
        UNABLE_TO_CHECK,
        UPGRADE_POSSIBLE
    }

    public AbstractMap.SimpleEntry<String, SpecStatus> serveLatestSpecIfPossible() throws FailedToProvideApiSpec {
        try {
            URL specUrl = new URL(SPEC_URL);

            if (!Files.exists(TARGET_PATH)) {
                downloadSpec();
                saveARecalculatedChecksum();
                String content = Files.readString(TARGET_PATH);

                return new AbstractMap.SimpleEntry<>(content, SpecStatus.FRESH);
            }

            String content = Files.readString(TARGET_PATH);

            // TODO: Use Result, monad or other things to avoid nested try catch blocks
            try {
                String oldChecksum = Files.readString(CHECKSUM_PATH);
                String newChecksum = calculateChecksum(specUrl, CHECKSUM_ALGORITHM);

                if (!newChecksum.equals(oldChecksum)) {
                    return new AbstractMap.SimpleEntry<>(newChecksum, SpecStatus.UPGRADE_POSSIBLE);
                }

                return  new AbstractMap.SimpleEntry<>(newChecksum, SpecStatus.FRESH);
            } catch (NoSuchAlgorithmException|IOException e) {
                return new AbstractMap.SimpleEntry<>(content, SpecStatus.UNABLE_TO_CHECK);
            }
        } catch (IOException e) {
            throw FailedToProvideApiSpec.dueToFileOperationIssue(e);
        } catch (DownloadFailed e) {
            throw FailedToProvideApiSpec.dueToDownloadFailure(e);
        } catch (NoSuchAlgorithmException e) {
            throw FailedToProvideApiSpec.dueTo("MD5 is not available", e);
       }
    }

    public String downloadAndGetLatestSpec() throws FailedToProvideApiSpec {
        try {
            downloadSpec();
            saveARecalculatedChecksum();
            String content = Files.readString(TARGET_PATH);

            return content;
        } catch (IOException e) {
            throw FailedToProvideApiSpec.dueToFileOperationIssue(e);
        } catch (DownloadFailed e) {
            throw FailedToProvideApiSpec.dueToDownloadFailure(e);
        } catch (NoSuchAlgorithmException e) {
            throw FailedToProvideApiSpec.dueTo("MD5 is not available", e);
        }
    }

    private void downloadSpec() throws DownloadFailed {
        try (InputStream in = new URL(SPEC_URL).openStream()) {
            Files.copy(in, TARGET_PATH, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new DownloadFailed("Failed to download " + TARGET_PATH.toAbsolutePath(), e);
        }
    }

    private void saveARecalculatedChecksum() throws IOException, NoSuchAlgorithmException {
        String checksum = calculateChecksum(TARGET_PATH);
        Files.writeString(CHECKSUM_PATH, checksum);
    }
}
