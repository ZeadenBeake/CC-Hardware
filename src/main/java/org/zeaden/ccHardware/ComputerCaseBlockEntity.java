package org.zeaden.ccHardware;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

// This class contains persistent information about computer cases. What's in it, what it's doin', all that jazz.
public class ComputerCaseBlockEntity extends BlockEntity {

    private final ComputerCasePeripheral perihperal = new ComputerCasePeripheral(this);

    public ComputerCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER_CASE_BLOCK_ENTITY, pos, state);
    }

    @Nullable
    public IPeripheral getPeripheral(Direction side) {
        return perihperal;
    }
}