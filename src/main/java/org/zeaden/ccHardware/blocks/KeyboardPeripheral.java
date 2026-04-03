package org.zeaden.ccHardware.blocks;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class KeyboardPeripheral implements IPeripheral {

    private final KeyboardBlockEntity blockEntity;
    private final Set<IComputerAccess> computers = new HashSet<>();

    public KeyboardPeripheral(KeyboardBlockEntity blockEntity) {
        this.blockEntity = blockEntity;
    }

    @Override
    public String getType() {
        return "keyboard";
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    // Called from the block's onUse when a player interacts
    public void sendKey(int keyCode, boolean held) {
        for (IComputerAccess computer : computers) {
            computer.queueEvent("key", keyCode, held);
        }
    }

    public void sendKeyUp(int keyCode) {
        for (IComputerAccess computer : computers) {
            computer.queueEvent("key_up", keyCode);
        }
    }

    public void sendChar(String character) {
        for (IComputerAccess computer : computers) {
            computer.queueEvent("char", character);
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        if (!(other instanceof KeyboardPeripheral o)) return false;
        return o.blockEntity == this.blockEntity;
    }
}