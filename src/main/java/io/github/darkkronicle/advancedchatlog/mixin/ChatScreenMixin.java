/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.mixin;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import io.github.darkkronicle.advancedchatcore.util.RawText;
import io.github.darkkronicle.advancedchatlog.gui.ChatLogScreen;
import io.github.darkkronicle.advancedchatlog.gui.CleanButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
@Environment(EnvType.CLIENT)
public class ChatScreenMixin {

    @Inject(method = "init", at = @At("RETURN"))
    public void initGui(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        String chatlog = StringUtils.translate("advancedchat.gui.button.chatlog");
        int chatlogWidth = StringUtils.getStringWidth(chatlog) + 5;
        int x = client.getWindow().getScaledWidth() - 3 - chatlogWidth;
        ColorUtil.SimpleColor color =
                new ColorUtil.SimpleColor(client.options.getTextBackgroundColor(-2147483648));
        CleanButton openChatLog =
                new CleanButton(
                        x,
                        client.getWindow().getScaledHeight() - 27,
                        chatlogWidth,
                        11,
                        new RawText(chatlog, Style.EMPTY),
                        color,
                        (button -> {
                            GuiBase.openGui(new ChatLogScreen());
                        }));
        ((ScreenInvoker) this).getChildren().add(openChatLog);
        ((ScreenInvoker) this).getDrawables().add(openChatLog);
    }
}
