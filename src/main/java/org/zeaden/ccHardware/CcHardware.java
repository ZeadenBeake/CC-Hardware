package org.zeaden.ccHardware;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.component.ComputerComponent;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.api.ModInitializer;

public class CcHardware implements ModInitializer {

    public static final String MOD_ID = "cc-hardware";
    public static final ComputerComponent<Boolean> CASE_COMPONENT =
            ComputerComponent.create(MOD_ID, "computer_case");
    @Override
    public void onInitialize() {
        ModItems.initialize();
        ModBlocks.initialize();
        ModBlockEntities.initialize();
        KeyInputPacket.register();
        ModMenuTypes.initialize();
        PeripheralLookup.get().registerForBlockEntity(
                (blockEntity, side) -> blockEntity.getPeripheral(),
                ModBlockEntities.KEYBOARD_BLOCK_ENTITY
        );

    }

}
