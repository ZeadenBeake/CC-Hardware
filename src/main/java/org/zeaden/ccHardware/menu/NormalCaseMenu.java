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
import org.zeaden.ccHardware.blocks.NormalCaseBlockEntity;
import org.zeaden.ccHardware.blocks.NormalCaseBlockEntity;

public class NormalCaseMenu extends AbstractContainerMenu {

    private final NormalCaseBlockEntity blockEntity;
    private final ContainerLevelAccess access;

    // Server-side constructor
    public NormalCaseMenu(int syncId, Inventory playerInventory, NormalCaseBlockEntity blockEntity) {
        super(ModMenuTypes.NORMAL_CASE_MENU, syncId);
        this.blockEntity = blockEntity;
        this.access = ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos());

        // Case slots
        // CPU slot (slot 0) - center top
        addSlot(new Slot(blockEntity, NormalCaseBlockEntity.SLOT_CPU, 80, 20));
        // RAM slots (1-2)
        addSlot(new Slot(blockEntity, NormalCaseBlockEntity.SLOT_RAM_1, 53, 50));
        addSlot(new Slot(blockEntity, NormalCaseBlockEntity.SLOT_RAM_2, 71, 50));
        // Storage slots (3-4)
        addSlot(new Slot(blockEntity, NormalCaseBlockEntity.SLOT_STORAGE_1, 89, 50));
        addSlot(new Slot(blockEntity, NormalCaseBlockEntity.SLOT_STORAGE_2, 107, 50));

        // Player inventory (3 rows of 9)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    // Client-side constructor (reads position from packet)
    public NormalCaseMenu(int syncId, Inventory playerInventory, FriendlyByteBuf buf) {
        this(syncId, playerInventory,
                (NormalCaseBlockEntity) playerInventory.player.level()
                        .getBlockEntity(buf.readBlockPos()));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.NORMAL_CASE);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Basic shift-click handling
        var slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        var stack = slot.getItem();
        var original = stack.copy();

        if (index < 5) {
            // Moving from case to player
            if (!moveItemStackTo(stack, 5, slots.size(), true)) return ItemStack.EMPTY;
        } else {
            // Moving from player to case
            if (!moveItemStackTo(stack, 0, 5, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return original;
    }
}