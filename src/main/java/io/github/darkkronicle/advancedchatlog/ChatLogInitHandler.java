/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import io.github.darkkronicle.advancedchatcore.chat.ChatHistory;
import io.github.darkkronicle.advancedchatcore.chat.ChatScreenSectionHolder;
import io.github.darkkronicle.advancedchatcore.config.gui.GuiConfigHandler;
import io.github.darkkronicle.advancedchatcore.hotkeys.InputHandler;
import io.github.darkkronicle.advancedchatlog.config.ChatLogConfigStorage;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreenSection;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ChatLogInitHandler implements IInitializationHandler {

    @Override
    public void registerModHandlers() {
        ConfigManager.getInstance()
                .registerConfigHandler(AdvancedChatLog.MOD_ID, new ChatLogConfigStorage());
        GuiConfigHandler.getInstance().addTab(GuiConfigHandler.wrapSaveableOptions(
                "log_general",
                "advancedchatlog.tab.general",
                ChatLogConfigStorage.General.OPTIONS
        ));

        // Register on new message
        ChatHistory.getInstance().addOnUpdate(ChatLogData.getInstance());
        // Register on the clear
        ChatHistory.getInstance().addOnClear(() -> ChatLogData.getInstance().normalClear());
        ChatScreenSectionHolder.getInstance().addSectionSupplier(ChatLogScreenSection::new);
        InputHandler.getInstance().addDisplayName("log_general", "advancedchatlog.config.tab.hotkeysgeneral");
        InputHandler.getInstance().add("log_general", ChatLogConfigStorage.Hotkeys.OPEN_LOG.config, (action, key) -> {
            GuiBase.openGui(new ChatLogScreen());
            return true;
        });
    }
}
