package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import org.zeaden.ccHardware.hardware.CpuItem;
import org.zeaden.ccHardware.hardware.RamItem;
import org.zeaden.ccHardware.hardware.StorageItem;

import java.util.function.Function;

public class ModItems {
    public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, new ResourceLocation(CcHardware.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings);

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static final Item SUSPICIOUS_SUBSTANCE = register(
            "suspicious_substance",
            Item::new,
            new Item.Properties()
    );
    public static final Item SCRENCH = register(
            "scrench",
            Item::new,
            new Item.Properties().stacksTo(1)
    );

    public static final Item CPU_T1 = register("cpu_t1", p -> new CpuItem(1, p), new Item.Properties());
    public static final Item CPU_T2 = register("cpu_t2", p -> new CpuItem(2, p), new Item.Properties());
    public static final Item CPU_T3 = register("cpu_t3", p -> new CpuItem(3, p), new Item.Properties());

    public static final Item RAM_T1 = register("ram_t1", p -> new RamItem(1, p), new Item.Properties());
    public static final Item RAM_T2 = register("ram_t2", p -> new RamItem(2, p), new Item.Properties());
    public static final Item RAM_T3 = register("ram_t3", p -> new RamItem(3, p), new Item.Properties());

    public static final Item STORAGE_T1 = register("storage_t1", p -> new StorageItem(1, p), new Item.Properties());
    public static final Item STORAGE_T2 = register("storage_t2", p -> new StorageItem(2, p), new Item.Properties());
    public static final Item STORAGE_T3 = register("storage_t3", p -> new StorageItem(3, p), new Item.Properties());

    public static void initialize() {
        // Get the event for modifying entries in the ingredients group.
        // And register an event handler that adds our suspicious item to the ingredients group.
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register((itemGroup) -> {
            itemGroup.accept(ModItems.SUSPICIOUS_SUBSTANCE);
            itemGroup.accept(ModItems.CPU_T1);
            itemGroup.accept(ModItems.CPU_T2);
            itemGroup.accept(ModItems.CPU_T3);
            itemGroup.accept(ModItems.RAM_T1);
            itemGroup.accept(ModItems.RAM_T2);
            itemGroup.accept(ModItems.RAM_T3);
            itemGroup.accept(ModItems.STORAGE_T1);
            itemGroup.accept(ModItems.STORAGE_T2);
            itemGroup.accept(ModItems.STORAGE_T3);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register((itemGroup) -> itemGroup.accept(ModItems.SCRENCH));
    }
}
