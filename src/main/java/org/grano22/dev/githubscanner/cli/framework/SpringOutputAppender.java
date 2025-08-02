package org.grano22.dev.githubscanner.cli.framework;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SpringOutputAppender extends AppenderBase<ILoggingEvent> {
    private Encoder<ILoggingEvent> encoder;

    @EventListener
    public void handleApplicationStart(ApplicationStartedEvent event) {
        SpringOutputCapturer.getInstance().appendOutput("Application started");
    }

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        SpringOutputCapturer.getInstance().appendOutput(iLoggingEvent.getMessage());
    }

    public void setEncoder(Encoder<ILoggingEvent> encoder) {
        this.encoder = encoder;
    }

    public Encoder<ILoggingEvent> getEncoder() {
        return encoder;
    }
}
