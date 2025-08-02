package org.grano22.dev.githubscanner.core.contract;

import jakarta.annotation.Nullable;
import org.grano22.dev.githubscanner.infrastructure.DownloadFailed;

import java.io.IOException;

public class FailedToProvideApiSpec extends Exception {
    public enum Reason {
        DOWNLOAD_FAILURE,
        FILE_SAVING_ISSUE,
        ANOTHER
    }

    private final Reason reason;

    public static FailedToProvideApiSpec dueToDownloadFailure(DownloadFailed downloadError) {
        return new FailedToProvideApiSpec(Reason.DOWNLOAD_FAILURE, "Source API was unavailable", downloadError);
    }

    public static FailedToProvideApiSpec dueToFileOperationIssue(IOException ioException) {
        return new FailedToProvideApiSpec(Reason.FILE_SAVING_ISSUE ,"Unable to save or read file", ioException);
    }

    public static FailedToProvideApiSpec dueTo(String externalReason, Exception ioException) {
        return new FailedToProvideApiSpec(Reason.ANOTHER, externalReason, ioException);
    }

    private FailedToProvideApiSpec(Reason reason, String message, @Nullable Throwable troubleMaker) {
        super(message, troubleMaker);

        this.reason = reason;
    }
}
