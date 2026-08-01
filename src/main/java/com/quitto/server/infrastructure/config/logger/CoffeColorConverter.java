package com.quitto.server.infrastructure.config.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.logging.logback.ColorConverter;
public class CoffeColorConverter extends ColorConverter{

    @Override
    protected String transform(ILoggingEvent event, String in) {
        Level level = event.getLevel();

        switch (level.toInt()) {

            case Level.TRACE_INT:
                return AnsiOutput.toString(AnsiColor.BRIGHT_BLACK, in);

            case Level.DEBUG_INT:
                return AnsiOutput.toString(AnsiColor.WHITE, in);

            case Level.INFO_INT:
                return AnsiOutput.toString(AnsiColor.WHITE, in);

            case Level.WARN_INT:
                return AnsiOutput.toString(AnsiColor.YELLOW, in);

            case Level.ERROR_INT:
                return AnsiOutput.toString(AnsiColor.RED, in);

            default:
                return in;
        }
    }
}
