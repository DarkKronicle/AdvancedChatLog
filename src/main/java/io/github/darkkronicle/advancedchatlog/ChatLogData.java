package io.github.darkkronicle.advancedchatlog;

import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import io.github.darkkronicle.advancedchatcore.interfaces.IChatMessageProcessor;
import io.github.darkkronicle.advancedchatlog.config.ChatLogConfigStorage;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ChatLogData implements IChatMessageProcessor {

    private final static ChatLogData INSTANCE = new ChatLogData();

    @Getter
    private List<ChatMessage> messages = new ArrayList<>();

    private ChatLogData() {

    }

    public static ChatLogData getInstance() {
        return INSTANCE;
    }

    private void add(ChatMessage message) {
        messages.add(0, message);
        Screen screen = MinecraftClient.getInstance().currentScreen;
        if (screen instanceof ChatLogScreen) {
            ((ChatLogScreen) screen).add(message);
        }
    }


    @Override
    public void onMessageUpdate(ChatMessage message, UpdateType type) {
        if (type != UpdateType.NEW) {
            return;
        }
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth() - 20;
        add(message.shallowClone(width));
        while (messages.size() > ChatLogConfigStorage.General.STORED_LINES.config.getIntegerValue()) {
            messages.remove(messages.size() - 1);
        }
    }

    public void clear() {
        // If someone wants their messages gone, lets respect that.
        messages.clear();
    }
}
