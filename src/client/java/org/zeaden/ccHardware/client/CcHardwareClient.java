package org.zeaden.ccHardware.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.zeaden.ccHardware.ClientProxy;

public class CcHardwareClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientProxy.setKeyboardScreenOpener(pos ->
                Minecraft.getInstance().setScreen(new KeyboardScreen(pos))
        );
    }

    public static void openKeyboardScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new KeyboardScreen(pos));
    }
}
