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
import io.github.darkkronicle.advancedchatcore.interfaces.AdvancedChatScreenSection;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class ChatLogScreenSection extends AdvancedChatScreenSection {

    public ChatLogScreenSection(AdvancedChatScreen screen) {
        super(screen);
    }

    @Override
    public void initGui() {
        ColorUtil.SimpleColor baseColor = ConfigStorage.ChatScreen.COLOR.config.getSimpleColor();
        String settings = StringUtils.translate("advancedchatlog.gui.button.log");
        int settingsWidth = StringUtils.getStringWidth(settings) + 5;
        int x = MinecraftClient.getInstance().getWindow().getScaledWidth() - 1;
        x -= settingsWidth + 5;
        io.github.darkkronicle.advancedchatcore.gui.CleanButton settingsButton =
                new CleanButton(
                        x,
                        MinecraftClient.getInstance().getWindow().getScaledHeight() - 39,
                        settingsWidth,
                        11,
                        baseColor,
                        settings);
        getScreen()
                .addButton(
                        settingsButton,
                        (button, mouseButton) -> GuiBase.openGui(new ChatLogScreen()));
    }
}
