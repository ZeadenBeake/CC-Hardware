package org.zeaden.ccHardware.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.zeaden.ccHardware.ClientProxy;
import org.zeaden.ccHardware.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;

public class CcHardwareClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientProxy.setKeyboardScreenOpener(pos ->
                Minecraft.getInstance().setScreen(new KeyboardScreen(pos)));

        MenuScreens.register(ModMenuTypes.NORMAL_CASE_MENU, NormalCaseScreen::new);
        MenuScreens.register(ModMenuTypes.ADVANCED_CASE_MENU, AdvancedCaseScreen::new);
    }


    public static void openKeyboardScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new KeyboardScreen(pos));
    }
}
