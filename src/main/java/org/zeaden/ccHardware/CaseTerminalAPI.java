package org.zeaden.ccHardware;

import dan200.computercraft.api.filesystem.WritableMount;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.apis.IAPIEnvironment;
import dan200.computercraft.core.apis.TermAPI;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeaden.ccHardware.blocks.AbstractCaseBlockEntity;
import org.zeaden.ccHardware.hardware.StorageItem;

public class CaseTerminalAPI extends TermAPI {

    private final AbstractCaseBlockEntity blockEntity;
    private static final Logger LOGGER = LoggerFactory.getLogger("cc-hardware");
    private final IAPIEnvironment environment;

    public CaseTerminalAPI(IAPIEnvironment environment, AbstractCaseBlockEntity blockEntity) {
        super(environment);
        this.environment = environment;
        this.blockEntity = blockEntity;
    }

    @Override
    public String[] getNames() {
        return new String[]{ "term" };
    }

    @LuaFunction({"native"})
    public final Object[] getNative() {
        var monitor = blockEntity.getActiveMonitor();
        return new Object[]{ monitor != null ? monitor : this };
    }

    @LuaFunction({"current"})
    public final Object[] getCurrent() {
        var monitor = blockEntity.getActiveMonitor();
        return new Object[]{ monitor != null ? monitor : this };
    }

    @LuaFunction({"isColour", "isColor"})
    public final boolean caseIsColour() {
        return blockEntity.getFamily() == ComputerFamily.ADVANCED;
    }

    @Override
    public void startup() {
        // Mount storage drives
        var fs = environment.getFileSystem();
        if (fs == null) return;

        var level = blockEntity.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return;

        int driveNum = 1;
        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
            var stack = blockEntity.getItem(i);
            if (stack.getItem() instanceof StorageItem storage) {
                var mount = storage.createDataMount(stack, serverLevel);
                if (mount instanceof WritableMount writable) {
                    try {
                        fs.mountWritable("disk" + driveNum, "disk" + driveNum, writable);
                        driveNum++;
                    } catch (Exception e) {
                        LOGGER.warn("Failed to mount storage drive {}", driveNum, e);
                    }
                }
            }
        }
    }
}