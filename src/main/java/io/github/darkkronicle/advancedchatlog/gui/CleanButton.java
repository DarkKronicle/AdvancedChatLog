/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.gui;

import fi.dy.masa.malilib.render.RenderUtils;
import io.github.darkkronicle.advancedchatcore.util.ColorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class CleanButton extends ButtonWidget {

    protected ColorUtil.SimpleColor baseColor;

    public CleanButton(
            int x,
            int y,
            int width,
            int height,
            Text message,
            ColorUtil.SimpleColor baseColor,
            PressAction onPress) {
        super(x, y, width, height, message, onPress);
        this.baseColor = baseColor;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int relMX = mouseX - x;
        int relMY = mouseY - y;
        hovered = relMX >= 0 && relMX <= width && relMY >= 0 && relMY <= height;
        ColorUtil.SimpleColor color = baseColor;
        if (hovered) {
            color = ColorUtil.WHITE.withAlpha(color.alpha());
        }
        RenderUtils.drawRect(x, y, width, height, color.color());
        drawCenteredText(
                matrices,
                MinecraftClient.getInstance().textRenderer,
                getMessage(),
                (x + (width / 2)),
                (y + (height / 2) - 3),
                ColorUtil.WHITE.color());
        if (this.isHovered()) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
    }
}
