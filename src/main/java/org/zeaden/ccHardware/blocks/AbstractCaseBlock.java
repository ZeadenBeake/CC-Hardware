package org.zeaden.ccHardware.blocks;

import com.mojang.serialization.MapCodec;
import dan200.computercraft.shared.computer.blocks.ComputerBlock;
import dan200.computercraft.shared.computer.core.ComputerState;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeaden.ccHardware.ModItems;

import static net.minecraft.world.level.levelgen.structure.Structure.simpleCodec;

public abstract class AbstractCaseBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<ComputerState> STATE = ComputerBlock.STATE;

    public AbstractCaseBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(STATE, ComputerState.OFF));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STATE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(ModItems.SCRENCH)) {
            if (!level.isClientSide()) {
                if (level.getBlockEntity(pos) instanceof AbstractCaseBlockEntity ccase) {
                    player.openMenu(new ExtendedScreenHandlerFactory() {
                        @Override
                        public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player p) {
                            return ccase.createMenu(syncId, inventory, p);
                        }

                        @Override
                        public Component getDisplayName() {
                            return ccase.getDisplayName();
                        }

                        @Override
                        public void writeScreenOpeningData(ServerPlayer serverPlayer, FriendlyByteBuf buf) {
                            buf.writeBlockPos(pos);
                        }
                    });
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        if (!level.isClientSide()) {
            if (level.getBlockEntity(pos) instanceof AbstractCaseBlockEntity ccase) {
                var computer = ccase.createServerComputer();
                if (computer.isOn()) {
                    computer.shutdown();
                } else {
                    if (!ccase.hasMinimumHardware()) {
                        // Tell the player what's missing
                        player.displayClientMessage(
                                Component.literal("Missing hardware: install a CPU, RAM, and storage."),
                                true  // true = action bar, false = chat
                        );
                    } else {
                        computer.turnOn();
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return (l, pos, s, be) -> {
            if (be instanceof AbstractCaseBlockEntity ccase) {
                ccase.tick();
            }
        };
    }
}