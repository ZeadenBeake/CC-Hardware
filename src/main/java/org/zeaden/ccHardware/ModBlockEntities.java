package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.zeaden.ccHardware.blocks.AbstractCaseBlockEntity;
import org.zeaden.ccHardware.blocks.AdvancedCaseBlockEntity;
import org.zeaden.ccHardware.blocks.KeyboardBlockEntity;
import org.zeaden.ccHardware.blocks.NormalCaseBlockEntity;

// Block entities factory
public class ModBlockEntities {

    public static BlockEntityType<KeyboardBlockEntity> KEYBOARD_BLOCK_ENTITY;
    public static BlockEntityType<NormalCaseBlockEntity> NORMAL_CASE_BLOCK_ENTITY;
    public static BlockEntityType<AdvancedCaseBlockEntity> ADVANCED_CASE_BLOCK_ENTITY;

    public static void initialize() {
        KEYBOARD_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(CcHardware.MOD_ID, "keyboard_full"),
                FabricBlockEntityTypeBuilder.create(
                        KeyboardBlockEntity::new,
                        ModBlocks.KEYBOARD_FULL
                ).build()
        );
        NORMAL_CASE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(CcHardware.MOD_ID, "normal_case"),
                FabricBlockEntityTypeBuilder.create(NormalCaseBlockEntity::new, ModBlocks.NORMAL_CASE).build()
        );
        ADVANCED_CASE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(CcHardware.MOD_ID, "advanced_case"),
                FabricBlockEntityTypeBuilder.create(AdvancedCaseBlockEntity::new, ModBlocks.ADVANCED_CASE).build()
        );
    }
}