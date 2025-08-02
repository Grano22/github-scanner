package org.grano22.dev.githubscanner.core.infrastructure;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculator {
    public static String calculateChecksum(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(filePath);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            while (dis.read() != -1) {}
        }
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));

        return sb.toString();
    }

    public static String calculateChecksum(URL url, String algorithm) throws IOException, NoSuchAlgorithmException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int contentLength = connection.getContentLength();

        return calculateChecksum(connection.getInputStream(), algorithm, contentLength);
    }

    public static String calculateChecksum(InputStream inputStream, String algorithm, int dataLength) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] buffer = new byte[dataLength > 0 ? Math.min(dataLength, 1024 * 1024) : 8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
