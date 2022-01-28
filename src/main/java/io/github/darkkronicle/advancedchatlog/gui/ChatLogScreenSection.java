/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen;
import io.github.darkkronicle.advancedchatcore.config.ConfigStorage;
import io.github.darkkronicle.advancedchatcore.gui.CleanButton;
import io.github.darkkronicle.advancedchatcore.gui.IconButton;
import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import io.github.darkkronicle.advancedchatcore.util.Color;
import io.github.darkkronicle.advancedchatlog.AdvancedChatLog;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ChatLogScreenSection extends AdvancedChatScreenSection {

    private final static Identifier LOG_ICON = new Identifier(AdvancedChatLog.MOD_ID, "textures/gui/log.png");

    public ChatLogScreenSection(AdvancedChatScreen screen) {
        super(screen);
    }

    @Override
    public void initGui() {
        getScreen().getRightSideButtons().add(
                "settings",
                new IconButton(0, 0, 14, 32, LOG_ICON,
                        (button) -> GuiBase.openGui(new ChatLogScreen())
                )
        );
    }
}
