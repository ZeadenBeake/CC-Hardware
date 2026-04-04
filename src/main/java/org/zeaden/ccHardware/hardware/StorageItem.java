package org.zeaden.ccHardware.hardware;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.filesystem.Mount;
import dan200.computercraft.api.media.IMedia;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StorageItem extends Item implements ICaseComponent, IMedia {

    private final int tier;

    public StorageItem(int tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override public int getTier() { return tier; }
    @Override public ComponentType getComponentType() { return ComponentType.STORAGE; }

    @Override
    public @Nullable String getLabel(ItemStack stack) {
        return stack.hasTag() ? stack.getTag().getString("Label") : null;
    }

    @Override
    public boolean setLabel(ItemStack stack, @Nullable String label) {
        if (label == null) {
            stack.removeTagKey("Label");
        } else {
            stack.getOrCreateTag().putString("Label", label);
        }
        return true;
    }

    @Override
    public @Nullable Mount createDataMount(ItemStack stack, ServerLevel level) {
        // Give each drive a unique ID stored in NBT
        var tag = stack.getOrCreateTag();
        if (!tag.contains("DriveId")) {
            tag.putInt("DriveId", (int)(System.nanoTime() & 0x7FFFFFFF));
        }
        int driveId = tag.getInt("DriveId");

        long sizeLimit = switch (tier) {
            case 1 -> 1L * 1024 * 1024;   // 1MB
            case 2 -> 4L * 1024 * 1024;   // 4MB
            case 3 -> 16L * 1024 * 1024;  // 16MB
            default -> 512L * 1024;
        };

        return ComputerCraftAPI.createSaveDirMount(
                level.getServer(),
                "computer/drive_" + driveId,
                sizeLimit
        );
    }
}