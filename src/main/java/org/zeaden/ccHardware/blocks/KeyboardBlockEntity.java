package org.zeaden.ccHardware.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.zeaden.ccHardware.ModBlockEntities;

public class KeyboardBlockEntity extends BlockEntity {

    private final KeyboardPeripheral peripheral = new KeyboardPeripheral(this);

    public KeyboardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEYBOARD_BLOCK_ENTITY, pos, state);
    }

    public KeyboardPeripheral getPeripheral() {
        return peripheral;
    }
}