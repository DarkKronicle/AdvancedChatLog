package io.github.darkkronicle.advancedchatlog.mixin;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenInvoker {

    @Accessor("children")
    List<Element> getChildren();

    @Accessor("drawables")
    List<Drawable> getDrawables();

}
