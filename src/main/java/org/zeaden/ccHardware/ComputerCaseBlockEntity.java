package org.zeaden.ccHardware;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashSet;
import java.util.Set;

// This class contains persistent information about computer cases. What's in it, what it's doin', all that jazz.
public class ComputerCaseBlockEntity extends ComputerBlockEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger("cc-hardware");

    private final Set<IComputerAccess> attachedComputers = new HashSet<>();
    private final ComputerCasePeripheral peripheral = new ComputerCasePeripheral(this);

    public ComputerCaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.COMPUTER_CASE_BLOCK_ENTITY, pos, state, ComputerFamily.NORMAL);
    }

    public ComputerCaseBlockEntity(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state, ComputerFamily.NORMAL);
    }

    @Override
    protected ServerComputer createComputer(int id) {
        return new ServerComputer(
                (ServerLevel) getLevel(), getBlockPos(),
                ServerComputer.properties(id, ComputerFamily.NORMAL)
                        .label(label)
                        //.terminalSize(new TerminalSize(51, 19))
        );
    }

    public void tick() {
        LOGGER.info("ticking case, computer id {}", getComputerID());
        if (getLevel() == null || getLevel().isClientSide()) return;
        var computer = createServerComputer();
        computer.keepAlive();

        if (!computer.isOn()) {
            computer.turnOn();
        }

        serverTick();
    }

    @Nullable
    public IPeripheral getPeripheral(Direction side) {
        return peripheral;
    }
}