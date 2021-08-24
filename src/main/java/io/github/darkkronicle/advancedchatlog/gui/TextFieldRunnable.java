package io.github.darkkronicle.advancedchatlog.gui;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import net.minecraft.client.font.TextRenderer;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class TextFieldRunnable extends GuiTextFieldGeneric {

    private final Consumer<TextFieldRunnable> onApply;

    public TextFieldRunnable(int x, int y, int width, int height, TextRenderer textRenderer, Consumer<TextFieldRunnable> onApply) {
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
