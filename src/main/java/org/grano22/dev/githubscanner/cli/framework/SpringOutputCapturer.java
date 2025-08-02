package org.grano22.dev.githubscanner.cli.framework;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SpringOutputCapturer {
    private final ConcurrentLinkedQueue<LogEntry> outputBuffer = new ConcurrentLinkedQueue<>();
    private static SpringOutputCapturer instance;

    private SpringOutputCapturer() {
        instance = this;
    }

    public static synchronized SpringOutputCapturer getInstance() {
        if (instance == null) {
            instance = new SpringOutputCapturer();
        }

        return instance;
    }

    private static class LogEntry {
        final LocalDateTime timestamp;
        final String message;
        final Thread thread;

        LogEntry(String message) {
            this.timestamp = LocalDateTime.now();
            this.message = message;
            this.thread = Thread.currentThread();
        }

        @Override
        public String toString() {
            return String.format("[%s] [%s] %s",
                    timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                    thread.getName(),
                    message);
        }
    }

    public void appendOutput(String message) {
        outputBuffer.offer(new LogEntry(message));
    }

    public String getOutputByThread(String threadName) {
        StringBuilder result = new StringBuilder();
        outputBuffer.stream()
                .filter(entry -> entry.thread.getName().equals(threadName))
                .forEach(entry -> result.append(entry.toString()).append("\n"));

        return result.toString();
    }

    public String getOutput() {
        StringBuilder result = new StringBuilder();

        outputBuffer.forEach(entry -> result.append(entry.toString()).append("\n"));

        return result.toString();
    }

    public void clearOutput() {
        outputBuffer.clear();
    }

    public int getMessageCount() {
        return outputBuffer.size();
    }
}
