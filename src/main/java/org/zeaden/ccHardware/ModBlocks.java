package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.zeaden.ccHardware.blocks.AdvancedCaseBlock;
import org.zeaden.ccHardware.blocks.KeyboardBlock;
import org.zeaden.ccHardware.blocks.NormalCaseBlock;

import java.util.function.Function;

public class ModBlocks {
    private static Block register(String name, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        ResourceKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings);

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            ResourceKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Properties());
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem);
        }

        return Registry.register(BuiltInRegistries.BLOCK, blockKey, block);
    }

    private static ResourceKey<Block> keyOfBlock(String name) {
        return ResourceKey.create(Registries.BLOCK, new ResourceLocation(CcHardware.MOD_ID, name));
    }

    private static ResourceKey<Item> keyOfItem(String name) {
        return ResourceKey.create(Registries.ITEM, new ResourceLocation(CcHardware.MOD_ID, name));
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register((itemGroup) -> {
            itemGroup.accept(ModBlocks.NORMAL_CASE.asItem());
            itemGroup.accept(ModBlocks.ADVANCED_CASE.asItem());
            itemGroup.accept(ModBlocks.KEYBOARD_FULL.asItem());
        });
    };

        public static final Block KEYBOARD_FULL = register(
            "keyboard_full",
            KeyboardBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.METAL),
            true
    );
    public static final Block NORMAL_CASE = register(
            "computer_case_normal",
            NormalCaseBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.METAL),
            true
    );
    public static final Block ADVANCED_CASE = register(
            "computer_case_advanced",
            AdvancedCaseBlock::new,
            BlockBehaviour.Properties.of().sound(SoundType.METAL),
            true
    );
}
