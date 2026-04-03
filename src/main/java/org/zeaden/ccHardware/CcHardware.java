package org.zeaden.ccHardware;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.api.ModInitializer;

public class CcHardware implements ModInitializer {

    public static final String MOD_ID = "cc-hardware";
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        ModBlockEntities.initialize();
//        PeripheralLookup.get().registerForBlockEntity(
//                ComputerCaseBlockEntity::getPeripheral,
//                ModBlockEntities.COMPUTER_CASE_BLOCK_ENTITY
//        );
    }
}
