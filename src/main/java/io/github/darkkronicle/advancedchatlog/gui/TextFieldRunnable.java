/*
 * Copyright (C) 2021 DarkKronicle
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.darkkronicle.advancedchatlog.gui;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import java.util.function.Consumer;
import net.minecraft.client.font.TextRenderer;
import org.lwjgl.glfw.GLFW;

public class TextFieldRunnable extends GuiTextFieldGeneric {

    private final Consumer<TextFieldRunnable> onApply;

    public TextFieldRunnable(
            int x,
            int y,
            int width,
            int height,
            TextRenderer textRenderer,
            Consumer<TextFieldRunnable> onApply) {
        super(x, y, width, height, textRenderer);
        this.onApply = onApply;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            onApply.accept(this);
            return true;
        }
        return false;
    }
}
