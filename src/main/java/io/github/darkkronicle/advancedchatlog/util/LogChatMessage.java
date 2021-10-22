package io.github.darkkronicle.advancedchatlog.util;

import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import java.time.LocalDate;
import lombok.Data;
import lombok.Getter;

@Data
public class LogChatMessage {

    private final ChatMessage message;

    private final LocalDate date;

    public LogChatMessage(ChatMessage message) {
        this(message, LocalDate.now());
    }

    public LogChatMessage(ChatMessage message, LocalDate date) {
        this.message = message;
        this.date = date;
    }
}
