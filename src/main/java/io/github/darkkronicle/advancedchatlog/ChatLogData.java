/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.github.darkkronicle.advancedchatcore.chat.ChatHistory;
import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.interfaces.IChatMessageProcessor;
import io.github.darkkronicle.advancedchatlog.config.ChatLogConfigStorage;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import io.github.darkkronicle.advancedchatlog.util.LogChatMessage;
import io.github.darkkronicle.advancedchatlog.util.LogChatMessageSerializer;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class ChatLogData implements IChatMessageProcessor {

    private static final ChatLogData INSTANCE = new ChatLogData();
    @Getter
    private static boolean loading = false;

    @Getter private List<LogChatMessage> messages = new ArrayList<>();

    private ChatLogData() {}

    public static ChatLogData getInstance() {
        return INSTANCE;
    }

    private void add(ChatMessage message) {
        LogChatMessage log = new LogChatMessage(message);
        messages.add(0, log);
        AdvancedChatLog.logChatMessage(message.getOriginalText());
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof ChatLogScreen) {
            ((ChatLogScreen) screen).add(log);
        }
    }

    private void add(LogChatMessage message) {
        messages.add(0, message);
    }

    @Override
    public void onMessageUpdate(ChatMessage message, UpdateType type) {
        if (loading) {
            return;
        }
        if (type != UpdateType.NEW) {
            return;
        }
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth() - 20;
        add(message.shallowClone(width));
        while (messages.size()
                > ChatLogConfigStorage.General.STORED_LINES.config.getIntegerValue()) {
            messages.remove(messages.size() - 1);
        }
    }

    /**
     * Method that AdvancedChatCore uses to clear. We can stop clearing from there if we want.
     */
    public void normalClear() {
        if (!ChatLogConfigStorage.General.ONLY_MANUAL_CLEAR.config.getBooleanValue()) {
            clear();
        }
    }

    public void clear() {
        // If someone wants their messages gone, lets respect that.
        messages.clear();
    }

    public JsonArray toJson() {
        int lines = ChatLogConfigStorage.General.SAVED_LINES.config.getIntegerValue();
        LogChatMessageSerializer serializer = new LogChatMessageSerializer();
        List<LogChatMessage> messages = new ArrayList<>();
        for (int i = 0; i < lines && i < ChatLogData.getInstance().getMessages().size(); i++) {
            messages.add(0, ChatLogData.getInstance().getMessages().get(i));
        }
        JsonArray array = new JsonArray();
        for (LogChatMessage message : messages) {
            try {
                array.add(serializer.save(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    public void load(JsonArray arr) {
        loading = true;
        messages.clear();
        LogChatMessageSerializer serializer = new LogChatMessageSerializer();

        for (int i = arr.size() - 1; i >= 0; i--) {
            JsonElement e = arr.get(i);
            if (!e.isJsonObject()) {
                continue;
            }
            try {
                messages.add(serializer.load(e.getAsJsonObject()));
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
        for (int i = Math.min(ChatLogConfigStorage.General.RELOAD_LINES.config.getIntegerValue() - 1, messages.size() - 1); i >= 0; i--) {
            ChatHistory.getInstance().add(messages.get(i).getMessage());
        }
        loading = false;
    }

    public LogChatMessage getLogMessage(ChatMessage message) {
        for (LogChatMessage log : messages) {
            if (log.getMessage().getUuid().equals(message.getUuid())) {
                return log;
            }
        }
        return null;
    }

}
