package org.zeaden.ccHardware.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.zeaden.ccHardware.ModBlocks;
import org.zeaden.ccHardware.ModMenuTypes;
import org.zeaden.ccHardware.blocks.AdvancedCaseBlockEntity;

public class AdvancedCaseMenu extends AbstractContainerMenu {

    private final AdvancedCaseBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    public AdvancedCaseMenu(int syncId, Inventory playerInventory, AdvancedCaseBlockEntity blockEntity) {
        super(ModMenuTypes.ADVANCED_CASE_MENU, syncId);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // CPU slot (0)
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_CPU, 80, 10));
        // RAM slots (1-4)
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_RAM_1, 44, 40));
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_RAM_2, 62, 40));
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_RAM_3, 80, 40));
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_RAM_4, 98, 40));
        // Storage slots (5-6)
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_STORAGE_1, 62, 68));
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_STORAGE_2, 80, 68));
        // Floppy slot (7)
        addSlot(new Slot(blockEntity, AdvancedCaseBlockEntity.SLOT_FLOPPY, 116, 40));

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 100 + row * 18));
            }
        }
        // Hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 158));
        }
    }

    public AdvancedCaseMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory,
                (AdvancedCaseBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.ADVANCED_CASE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        var stack = slot.getItem();
        var original = stack.copy();

        if (index < 8) {
            if (!moveItemStackTo(stack, 8, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            if (!moveItemStackTo(stack, 0, 8, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return original;
    }
}