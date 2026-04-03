package org.zeaden.ccHardware;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.apis.IAPIEnvironment;
import dan200.computercraft.core.apis.TermAPI;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import org.zeaden.ccHardware.blocks.AbstractCaseBlockEntity;

public class CaseTerminalAPI extends TermAPI {

    private final AbstractCaseBlockEntity blockEntity;

    public CaseTerminalAPI(IAPIEnvironment environment, AbstractCaseBlockEntity blockEntity) {
        super(environment);
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
}