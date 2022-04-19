/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.ModuleHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Environment(EnvType.CLIENT)
public class AdvancedChatLog implements ClientModInitializer {

    public static final String MOD_ID = "advancedchatlog";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    // Use java's built in since this is *simple*
    private static org.slf4j.Logger CHAT_LOGGER;
    private final static SimpleDateFormat DATE_TIME = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onInitializeClient() {
        // This will run after AdvancedChatCore's because of load order
        ModuleHandler.getInstance().registerInitHandler(MOD_ID, 1, new ChatLogInitHandler());
        setupLogger();
    }

    private static void setupLogger() {
        File dir = FileUtils.getMinecraftDirectory().toPath().resolve("chatlogs").toFile();
        dir.mkdirs();
        CHAT_LOGGER = LoggerFactory.getLogger("chat");
        LogManager.getLogger("chat");
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);

//        FileAppender fileAppender =  FileAppender
//                .newBuilder()
//                .withFileName("chatlogs/latest.log")
//                .setName("chatlog")
//                .withBufferSize(100000)
//                .withImmediateFlush(true)
//                .withAppend(true)
//                .build();
//        fileAppender.start();
        RollingRandomAccessFileAppender rolling = RollingRandomAccessFileAppender
                .newBuilder()
                .setName("chatlogFile")
                .withFileName("chatlogs/latest.log")
                .withBufferSize(100000)
                .withPolicy(TimeBasedTriggeringPolicy.newBuilder().withInterval(1).build())
                .withPolicy(OnStartupTriggeringPolicy.createPolicy(1000))
                .withAppend(true)
                .withImmediateFlush(true)
                .withFilePattern("chatlogs/%d{yyyy-MM-dd}-%i.log.gz").build();

        org.apache.logging.log4j.core.Logger logger = loggerContext.getLogger("chat");
        rolling.start();
        logger.addAppender(rolling);
        loggerContext.updateLoggers();
    }

    public static void logChatMessage(Text text) {
        if (ChatLogData.isLoading()) {
            return;
        }
        String message = text.getString();
        message = "[" + DATE_TIME.format(new Date()) + "] " + message;
        CHAT_LOGGER.info(message);
    }
}
