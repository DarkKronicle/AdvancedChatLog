package io.github.darkkronicle.advancedchatlog.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.darkkronicle.advancedchatcore.chat.ChatMessage;
import io.github.darkkronicle.advancedchatcore.interfaces.IJsonSave;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import io.github.darkkronicle.advancedchatcore.util.FluidText;
import io.github.darkkronicle.advancedchatlog.config.ChatLogConfigStorage;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class LogChatMessageSerializer implements IJsonSave<LogChatMessage> {

    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public LogChatMessageSerializer() {}

    private Style cleanStyle(Style style) {
        if (!ChatLogConfigStorage.General.CLEAN_SAVE.config.getBooleanValue()) {
            return style;
        }
        style = style.withClickEvent(null);
        style = style.withHoverEvent(null);
        style = style.withInsertion(null);
        return style;
    }

    private LiteralText transfer(Text text) {
        // Using the built in serializer LiteralText is required
        LiteralText base = new LiteralText("");
        for (Text t : text.getSiblings()) {
            LiteralText newT = new LiteralText(t.getString());
            newT.setStyle(cleanStyle(t.getStyle()));
            base.append(newT);
        }
        return base;
    }

    @Override
    public LogChatMessage load(JsonObject obj) {
        LocalDateTime dateTime = LocalDateTime.from(
            formatter.parse(obj.get("time").getAsString())
        );
        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();
        Text display = Text.Serializer.fromJson(obj.get("display"));
        Text original = Text.Serializer.fromJson(obj.get("original"));
        int stacks = obj.get("stacks").getAsByte();
        ChatMessage message = ChatMessage
            .builder()
            .time(time)
            .displayText(display)
            .originalText(original)
            .build();
        LogChatMessage log = new LogChatMessage(message, date);
        return log;
    }

    @Override
    public JsonObject save(LogChatMessage message) {
        JsonObject json = new JsonObject();
        ChatMessage chat = message.getMessage();
        LocalDateTime dateTime = LocalDateTime.of(
            message.getDate(),
            chat.getTime()
        );
        json.addProperty("time", formatter.format(dateTime));
        json.addProperty("stacks", chat.getStacks());
        json.add(
            "display",
            Text.Serializer.toJsonTree(transfer(chat.getDisplayText()))
        );
        json.add(
            "original",
            Text.Serializer.toJsonTree(transfer(chat.getOriginalText()))
        );
        return json;
    }
}
