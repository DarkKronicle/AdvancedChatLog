/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.config.SaveableConfig;
import io.github.darkkronicle.advancedchatlog.AdvancedChatLog;
import io.github.darkkronicle.advancedchatlog.ChatLogData;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatLogConfigStorage implements IConfigHandler {

    public static final String CONFIG_FILE_NAME = AdvancedChatLog.MOD_ID + ".json";
    private static final int CONFIG_VERSION = 1;

    public static class General {

        public static final String NAME = "general";

        public static String translate(String key) {
            return StringUtils.translate("advancedchatlog.config.general." + key);
        }

        public static final SaveableConfig<ConfigInteger> STORED_LINES =
                SaveableConfig.fromConfig(
                        "stored_lines",
                        new ConfigInteger(
                                translate("stored_lines"),
                                1000,
                                50,
                                10000,
                                translate("info.stored_lines")));

        public static final SaveableConfig<ConfigBoolean> ONLY_MANUAL_CLEAR =
                SaveableConfig.fromConfig(
                        "only_manual_clear",
                        new ConfigBoolean(
                                translate("only_manual_clear"),
                                true,
                                translate("info.only_manual_clear")));

        public static final SaveableConfig<ConfigInteger> SCROLL_TIME =
                SaveableConfig.fromConfig(
                        "scroll_time",
                        new ConfigInteger(
                                translate("scroll_time"),
                                200,
                                0,
                                2000,
                                translate("info.scroll_time")));

        public static final SaveableConfig<ConfigOptionList> SCROLL_TYPE =
                SaveableConfig.fromConfig(
                        "scroll_type",
                        new ConfigOptionList(
                                translate("scroll_type"),
                                ConfigStorage.Easing.QUART,
                                translate("info.scroll_time")));

        public static final SaveableConfig<ConfigDouble> SCROLL_MULTIPLIER =
                SaveableConfig.fromConfig(
                        "scroll_multiplier",
                        new ConfigDouble(
                                translate("scroll_multiplier"),
                                5,
                                0,
                                100,
                                translate("info.scroll_multiplier")));

        public static final SaveableConfig<ConfigInteger> SAVED_LINES =
                SaveableConfig.fromConfig(
                        "saved_lines",
                        new ConfigInteger(
                                translate("saved_lines"),
                                0,
                                0,
                                10000,
                                translate("info.saved_lines")));

        public static final SaveableConfig<ConfigInteger> RELOAD_LINES =
                SaveableConfig.fromConfig(
                        "reload_lines",
                        new ConfigInteger(
                                translate("reload_lines"),
                                0,
                                0,
                                10000,
                                translate("info.reload_lines")));

        public static final SaveableConfig<ConfigBoolean> CLEAN_SAVE =
                SaveableConfig.fromConfig(
                        "clean_save",
                        new ConfigBoolean(
                                translate("clean_save"), false, translate("info.clean_save")));

        public static final ImmutableList<SaveableConfig<? extends IConfigBase>> OPTIONS =
                ImmutableList.of(STORED_LINES, ONLY_MANUAL_CLEAR, SAVED_LINES, RELOAD_LINES, CLEAN_SAVE, SCROLL_TIME, SCROLL_TYPE, SCROLL_MULTIPLIER);
    }

    public static class Hotkeys {
        public static final String NAME = "hotkeys";

        public static String translate(String key) {
            return StringUtils.translate("advancedchatlog.config.hotkeys." + key);
        }

        public static final SaveableConfig<ConfigHotkey> OPEN_LOG = SaveableConfig.fromConfig("openSettings",
                new ConfigHotkey(translate("openlog"), "U", KeybindSettings.create(
                        KeybindSettings.Context.INGAME, KeyAction.PRESS, false, true, false, true
                ), translate("info.openlog")));

        public static final ImmutableList<ConfigHotkey> HOTKEYS =
                ImmutableList.of(OPEN_LOG.config);

        public static final ImmutableList<SaveableConfig<? extends IConfigBase>> OPTIONS =
                ImmutableList.of(OPEN_LOG);
    }

    public static void loadFromFile() {
        File configFile =
                FileUtils.getConfigDirectory()
                        .toPath()
                        .resolve("advancedchat")
                        .resolve(CONFIG_FILE_NAME)
                        .toFile();
        File savedFile =
                FileUtils.getConfigDirectory()
                        .toPath()
                        .resolve("advancedchat")
                        .resolve("saved_lines.json")
                        .toFile();

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = ConfigStorage.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigStorage.readOptions(root, General.NAME, (List<SaveableConfig<?>>) General.OPTIONS);
                ConfigStorage.readOptions(root, Hotkeys.NAME, (List<SaveableConfig<?>>) Hotkeys.OPTIONS);

                int version = JsonUtils.getIntegerOrDefault(root, "configVersion", 0);
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
        File dir = FileUtils.getConfigDirectory().toPath().resolve("advancedchat").toFile();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigStorage.writeOptions(root, General.NAME, (List<SaveableConfig<?>>) General.OPTIONS);
            ConfigStorage.writeOptions(root, Hotkeys.NAME, (List<SaveableConfig<?>>) Hotkeys.OPTIONS);

            root.add("config_version", new JsonPrimitive(CONFIG_VERSION));

            ConfigStorage.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));

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
