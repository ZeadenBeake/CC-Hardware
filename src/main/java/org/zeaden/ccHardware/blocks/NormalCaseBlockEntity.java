package org.zeaden.ccHardware.blocks;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.zeaden.ccHardware.ModBlockEntities;

public class NormalCaseBlockEntity extends AbstractCaseBlockEntity {

    // Slots: 0=CPU, 1-2=RAM, 3-4=Storage
    public static final int SLOT_CPU = 0;
    public static final int SLOT_RAM_1 = 1;
    public static final int SLOT_RAM_2 = 2;
    public static final int SLOT_STORAGE_1 = 3;
    public static final int SLOT_STORAGE_2 = 4;
    public static final int INVENTORY_SIZE = 5;

    public NormalCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NORMAL_CASE_BLOCK_ENTITY, pos, state, ComputerFamily.NORMAL, INVENTORY_SIZE);
    }

    @Override
    public ComputerFamily getFamily() {
        return ComputerFamily.NORMAL;
    }
}