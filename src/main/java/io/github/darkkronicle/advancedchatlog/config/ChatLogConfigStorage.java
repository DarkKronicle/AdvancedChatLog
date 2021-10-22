package io.github.darkkronicle.advancedchatlog.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatlog.AdvancedChatLog;
import io.github.darkkronicle.advancedchatlog.ChatLogData;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatLogConfigStorage implements IConfigHandler {

    public static final String CONFIG_FILE_NAME =
        AdvancedChatLog.MOD_ID + ".json";
    private static final int CONFIG_VERSION = 1;

    public static class General {

        public static final String NAME = "general";

        public static String translate(String key) {
            return StringUtils.translate(
                "advancedchatlog.config.general." + key
            );
        }

        public static final ConfigStorage.SaveableConfig<ConfigInteger> STORED_LINES = ConfigStorage.SaveableConfig.fromConfig(
            "stored_lines",
            new ConfigInteger(
                translate("stored_lines"),
                1000,
                50,
                10000,
                translate("info.stored_lines")
            )
        );

        public static final ConfigStorage.SaveableConfig<ConfigInteger> SAVED_LINES = ConfigStorage.SaveableConfig.fromConfig(
            "saved_lines",
            new ConfigInteger(
                translate("saved_lines"),
                0,
                0,
                10000,
                translate("info.saved_lines")
            )
        );

        public static final ConfigStorage.SaveableConfig<ConfigBoolean> CLEAN_SAVE = ConfigStorage.SaveableConfig.fromConfig(
            "clean_save",
            new ConfigBoolean(
                translate("clean_save"),
                false,
                translate("info.clean_save")
            )
        );

        public static final ImmutableList<ConfigStorage.SaveableConfig<? extends IConfigBase>> OPTIONS = ImmutableList.of(
            STORED_LINES,
            SAVED_LINES,
            CLEAN_SAVE
        );
    }

    public static void loadFromFile() {
        File configFile = FileUtils
            .getConfigDirectory()
            .toPath()
            .resolve("advancedchat")
            .resolve(CONFIG_FILE_NAME)
            .toFile();
        File savedFile = FileUtils
            .getConfigDirectory()
            .toPath()
            .resolve("advancedchat")
            .resolve("saved_lines.json")
            .toFile();

        if (
            configFile.exists() && configFile.isFile() && configFile.canRead()
        ) {
            JsonElement element = ConfigStorage.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigStorage.readOptions(
                    root,
                    General.NAME,
                    (List<ConfigStorage.SaveableConfig<?>>) General.OPTIONS
                );

                int version = JsonUtils.getIntegerOrDefault(
                    root,
                    "configVersion",
                    0
                );
            }
        }

        if (savedFile.exists() && savedFile.isFile() && savedFile.canRead()) {
            JsonElement element = ConfigStorage.parseJsonFile(savedFile);
            if (element != null && element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (!obj.has("lines")) {
                    return;
                }
                JsonElement el = obj.get("lines");
                if (!el.isJsonArray()) {
                    return;
                }
                ChatLogData.getInstance().load(el.getAsJsonArray());
            }
        }
    }

    public static void saveFromFile() {
        File dir = FileUtils
            .getConfigDirectory()
            .toPath()
            .resolve("advancedchat")
            .toFile();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigStorage.writeOptions(
                root,
                General.NAME,
                (List<ConfigStorage.SaveableConfig<?>>) General.OPTIONS
            );

            root.add("config_version", new JsonPrimitive(CONFIG_VERSION));

            ConfigStorage.writeJsonToFile(
                root,
                new File(dir, CONFIG_FILE_NAME)
            );

            OutputStreamWriter writer = null;

            File file = new File(dir, "saved_lines.json");
            JsonObject saved = new JsonObject();
            saved.add("lines", ChatLogData.getInstance().toJson());
            ConfigStorage.writeJsonToFile(saved, file);
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveFromFile();
    }
}
