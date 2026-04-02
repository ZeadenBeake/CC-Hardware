package org.zeaden.ccHardware;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;

public class ComputerCasePeripheral implements IPeripheral {

    private final ComputerCaseBlockEntity blockEntity;

    public ComputerCasePeripheral(ComputerCaseBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public String getType() {
        return "computer_case";
    }

    @LuaFunction
    public final String ping() {
        return "pong!";
    }

    @Override
    public boolean equals(IPeripheral other) {
        if (!(other instanceof ComputerCasePeripheral otherPeripheral)) return false;
        return otherPeripheral.blockEntity == this.blockEntity;
    }
}