package org.zeaden.ccHardware.blocks;

import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import dan200.computercraft.shared.peripheral.monitor.MonitorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeaden.ccHardware.CaseMetaAPI;
import org.zeaden.ccHardware.CaseTerminalAPI;
import org.zeaden.ccHardware.CcHardware;

public abstract class AbstractCaseBlockEntity extends ComputerBlockEntity {

    private static final Logger LOGGER = LoggerFactory.getLogger("cc-hardware");

    private final NonNullList<ItemStack> inventory;
    private @Nullable IPeripheral activeMonitor = null;
    private int lastMonitorWidth = -1;
    private int lastMonitorHeight = -1;

    @SuppressWarnings("unchecked")
    public AbstractCaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, ComputerFamily family, int inventorySize) {
        super((BlockEntityType<? extends ComputerBlockEntity>) type, pos, state, family);
        this.inventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
    }

    public abstract ComputerFamily getFamily();

    @Override
    protected ServerComputer createComputer(int id) {
        var serverComputer = new ServerComputer(
                (ServerLevel) getLevel(), getBlockPos(),
                ServerComputer.properties(id, getFamily())
                        .label(label)
                        .terminalSize(51, 19)
        );

        try {
            var computerField = ServerComputer.class.getDeclaredField("computer");
            computerField.setAccessible(true);
            var computer = (dan200.computercraft.core.computer.Computer) computerField.get(serverComputer);
            computer.addApi(new CaseTerminalAPI(computer.getAPIEnvironment(), this));
            computer.addApi(new CaseMetaAPI(getFamily() == ComputerFamily.ADVANCED));
            LOGGER.info("Successfully injected CaseTerminalAPI");
        } catch (Exception e) {
            LOGGER.warn("Failed to inject CaseTerminalAPI", e);
        }
        LOGGER.info("Creating computer with family: {}", getFamily());
        return serverComputer;
    }

    public void tick() {
        if (getLevel() == null || getLevel().isClientSide()) return;

        // Scan for monitor
        IPeripheral found = null;
        BlockPos monitorPos = null;
        for (var dir : Direction.values()) {
            var peripheral = PeripheralLookup.get()
                    .find((ServerLevel) getLevel(), getBlockPos().relative(dir), dir.getOpposite());
            if (peripheral != null && peripheral.getType().equals("monitor")) {
                found = peripheral;
                monitorPos = getBlockPos().relative(dir);
                break;
            }
        }
        activeMonitor = found;

        // Check terminal dimensions for resize events
        if (monitorPos != null) {
            var be = getLevel().getBlockEntity(monitorPos);
            if (be instanceof MonitorBlockEntity monitor) {
                var serverMonitor = monitor.getCachedServerMonitor();
                if (serverMonitor != null) {
                    var terminal = serverMonitor.getTerminal();
                    if (terminal != null) {
                        int w = terminal.getWidth();
                        int h = terminal.getHeight();
                        if (w != lastMonitorWidth || h != lastMonitorHeight) {
                            lastMonitorWidth = w;
                            lastMonitorHeight = h;
                            var sc = getServerComputer();
                            if (sc != null && sc.isOn()) sc.queueEvent("term_resize");
                        }
                    }
                }
            }
        }

        serverTick();
    }

    public @Nullable IPeripheral getActiveMonitor() {
        return activeMonitor;
    }

    // Inventory
    public int getInventorySize() {
        return inventory.size();
    }

    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    public ItemStack removeItem(int slot, int count) {
        var stack = ContainerHelper.removeItem(inventory, slot, count);
        if (!stack.isEmpty()) setChanged();
        return stack;
    }

    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, inventory);
    }

    @Override
    protected void loadServer(CompoundTag tag) {
        super.loadServer(tag);
        ContainerHelper.loadAllItems(tag, inventory);
    }
}