package org.zeaden.ccHardware;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.item.Item;

public class CcHardware implements ModInitializer {

    public static final String MOD_ID = "cc-hardware";
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        ModBlockEntities.initialize();
    }
}
