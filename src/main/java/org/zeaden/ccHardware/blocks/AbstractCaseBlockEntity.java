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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeaden.ccHardware.CaseMetaAPI;
import org.zeaden.ccHardware.CaseTerminalAPI;
import org.zeaden.ccHardware.hardware.ComponentType;
import org.zeaden.ccHardware.hardware.ICaseComponent;

public abstract class AbstractCaseBlockEntity extends ComputerBlockEntity implements Container, MenuProvider {

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

    @Override
    public ComputerFamily getFamily() {
        var cpu = getInstalledCPU();
        if (cpu != null && cpu.getTier() >= 3) return ComputerFamily.ADVANCED;
        return ComputerFamily.NORMAL;
    }

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

        return serverComputer;
    }

    public void tick() {
        if (getLevel() == null || getLevel().isClientSide()) return;

        var serverComputer = getServerComputer();
        if (serverComputer != null && serverComputer.isOn()) {
            // Shut down if CPU is removed
            if (getInstalledCPU() == null) {
                serverComputer.shutdown();
                return;
            }
        }

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

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        var stack = ContainerHelper.removeItem(inventory, slot, count);
        if (!stack.isEmpty()) setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(inventory, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        inventory.clear();
        setChanged();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getBlockState().getBlock().getDescriptionId());
    }

    // NormalCaseBlockEntity overrides this:
    @Override
    public abstract AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player);

    public @Nullable IPeripheral getActiveMonitor() {
        return activeMonitor;
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

    @Nullable
    private ICaseComponent getComponent(int slot, ComponentType expectedType) {
        var stack = getItem(slot);
        if (stack.isEmpty()) return null;
        if (!(stack.getItem() instanceof ICaseComponent component)) return null;
        if (component.getComponentType() != expectedType) return null;
        return component;
    }

    public @Nullable ICaseComponent getInstalledCPU() {
        return getComponent(0, ComponentType.CPU);
    }

    // Sum tiers across all RAM slots
    public int getTotalRAMTier() {
        int total = 0;
        for (int i = 1; i < getContainerSize(); i++) {
            var component = getComponent(i, ComponentType.RAM);
            if (component != null) total += component.getTier();
        }
        return total;
    }

    // Get highest storage tier installed
    public int getHighestStorageTier() {
        int highest = 0;
        for (int i = 1; i < getContainerSize(); i++) {
            var component = getComponent(i, ComponentType.STORAGE);
            if (component != null) highest = Math.max(highest, component.getTier());
        }
        return highest;
    }

    public boolean hasMinimumHardware() {
        if (getInstalledCPU() == null) return false;
        if (getTotalRAMTier() == 0) return false;
        return true;
    }
}