/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.gui.GuiBase;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class AdvancedChatLog implements ClientModInitializer {

    public static final String MOD_ID = "advancedchatlog";

    @Override
    public void onInitializeClient() {
        // This will run after AdvancedChatCore's because of load order
        InitializationHandler.getInstance().registerInitializationHandler(new ChatLogInitHandler());

        KeyBinding keyBinding =
                new KeyBinding(
                        "advancedchatlog.key.openlog",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "advancedchat.category.keys");
        KeyBindingHelper.registerKeyBinding(keyBinding);
        ClientTickEvents.START_CLIENT_TICK.register(
                s -> {
                    if (keyBinding.wasPressed()) {
                        GuiBase.openGui(new ChatLogScreen());
                    }
                });
    }
}
