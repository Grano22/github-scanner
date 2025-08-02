package org.grano22.dev.githubscanner.infrastructure;

public class DownloadFailed extends Exception {
    public DownloadFailed(String message, Throwable cause) {
        super(message, cause);
    }
}
