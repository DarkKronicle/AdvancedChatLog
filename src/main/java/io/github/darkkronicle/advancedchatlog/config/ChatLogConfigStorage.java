package io.github.darkkronicle.advancedchatlog.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.config.options.ConfigString;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatlog.AdvancedChatLog;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.io.File;
import java.util.List;


@Environment(EnvType.CLIENT)
public class ChatLogConfigStorage implements IConfigHandler {

    public static final String CONFIG_FILE_NAME = AdvancedChatLog.MOD_ID + ".json";
    private static final int CONFIG_VERSION = 1;

    public static class General {

        public static final String NAME = "general";

        public static String translate(String key) {
            return StringUtils.translate("advancedchatlog.config.general." + key);
        }

        public final static ConfigStorage.SaveableConfig<ConfigInteger> STORED_LINES = ConfigStorage.SaveableConfig.fromConfig("stored_lines",
                new ConfigInteger(translate("stored_lines"), 1000, 50, 10000, translate("info.storedlines")));

        public final static ImmutableList<ConfigStorage.SaveableConfig<? extends IConfigBase>> OPTIONS = ImmutableList.of(
                STORED_LINES
        );

    }

    public static void loadFromFile() {

        File configFile = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").resolve(CONFIG_FILE_NAME).toFile();

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = ConfigStorage.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigStorage.readOptions(root, General.NAME, (List<ConfigStorage.SaveableConfig<?>>) General.OPTIONS);

                int version = JsonUtils.getIntegerOrDefault(root, "configVersion", 0);

            }
        }
    }

    public static void saveFromFile() {
        File dir = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").toFile();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigStorage.writeOptions(root, General.NAME, (List<ConfigStorage.SaveableConfig<?>>) General.OPTIONS);

            root.add("config_version", new JsonPrimitive(CONFIG_VERSION));

            ConfigStorage.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
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
