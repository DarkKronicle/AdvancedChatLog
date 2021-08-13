package io.github.darkkronicle.advancedchatlog;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AdvancedChatLog implements ClientModInitializer {

    public static final String MOD_ID = "advancedchatlog";

    @Override
    public void onInitializeClient() {
        // This will run after AdvancedChatCore's because of load order
        InitializationHandler.getInstance().registerInitializationHandler(new ChatLogInitHandler());
    }

}
