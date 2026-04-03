package org.zeaden.ccHardware.blocks;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.zeaden.ccHardware.ModBlockEntities;

public class AdvancedCaseBlockEntity extends AbstractCaseBlockEntity {

    // Slots: 0=CPU, 1-4=RAM, 5-6=Storage, 7=Floppy
    public static final int SLOT_CPU = 0;
    public static final int SLOT_RAM_1 = 1;
    public static final int SLOT_RAM_2 = 2;
    public static final int SLOT_RAM_3 = 3;
    public static final int SLOT_RAM_4 = 4;
    public static final int SLOT_STORAGE_1 = 5;
    public static final int SLOT_STORAGE_2 = 6;
    public static final int SLOT_FLOPPY = 7;
    public static final int INVENTORY_SIZE = 8;

    public AdvancedCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ADVANCED_CASE_BLOCK_ENTITY, pos, state, ComputerFamily.ADVANCED, INVENTORY_SIZE);
    }

    @Override
    public ComputerFamily getFamily() {
        return ComputerFamily.ADVANCED;
    }
}