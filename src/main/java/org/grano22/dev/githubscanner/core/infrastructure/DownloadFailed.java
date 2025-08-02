package org.grano22.dev.githubscanner.core.infrastructure;

public class DownloadFailed extends Exception {
    public DownloadFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
