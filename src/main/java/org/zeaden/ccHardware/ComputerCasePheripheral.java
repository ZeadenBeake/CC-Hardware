package org.zeaden.ccHardware;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;

public class ComputerCasePheripheral implements IPeripheral {
    @Override
    public String getType() {
        return "computer_case";  // what peripheral.getType() returns in Lua
    }

    @LuaFunction
    public final String hello() {
        return "Hello from CC:Hardware!";
    }

    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }
}
