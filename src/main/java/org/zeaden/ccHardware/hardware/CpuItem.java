package org.zeaden.ccHardware.hardware;

import net.minecraft.world.item.Item;

public class CpuItem extends Item implements ICaseComponent {
    private final int tier;

    public CpuItem(int tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    @Override public int getTier() { return tier; }
    @Override public ComponentType getComponentType() { return ComponentType.CPU; }
}