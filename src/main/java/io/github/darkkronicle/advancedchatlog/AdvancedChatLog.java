/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.FileUtils;
import io.github.darkkronicle.advancedchatcore.ModuleHandler;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.RegexFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.Message;
import org.lwjgl.glfw.GLFW;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.logging.FileHandler;

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

        KeyBinding keyBinding =
                new KeyBinding(
                        "advancedchatlog.key.openlog",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "advancedchat.category.keys");
        KeyBindingHelper.registerKeyBinding(keyBinding);
        ClientTickEvents.START_CLIENT_TICK.register(
                s -> {
                    if (keyBinding.wasPressed()) {
                        GuiBase.openGui(new ChatLogScreen());
                    }
                });
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
        String message = text.getString();
        message = "[" + DATE_TIME.format(new Date()) + "] " + message;
        CHAT_LOGGER.info(message);
    }
}
