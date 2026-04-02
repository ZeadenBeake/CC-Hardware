package org.zeaden.ccHardware;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

// This class contains persistent information about computer cases. What's in it, what it's doin', all that jazz.
public class ComputerCaseBlockEntity extends BlockEntity {

    public ComputerCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER_CASE_BLOCK_ENTITY, pos, state);
    }
}