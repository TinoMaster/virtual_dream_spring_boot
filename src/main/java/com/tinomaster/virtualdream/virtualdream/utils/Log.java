package com.tinomaster.virtualdream.virtualdream.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class Log {

    private static final Logger LOGGER = Logger.getLogger(Log.class.getName());

    public static void info(String message) {
        LOGGER.info(message);
    }
}
