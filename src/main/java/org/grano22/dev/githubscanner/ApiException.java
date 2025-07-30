package org.grano22.dev.githubscanner;

public class ApiException extends RuntimeException {
    private final int code;

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
