package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

// Block entities factory
public class ModBlockEntities {

    public static BlockEntityType<ComputerCaseBlockEntity> COMPUTER_CASE_BLOCK_ENTITY;

    public static void initialize() {
        COMPUTER_CASE_BLOCK_ENTITY = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                new ResourceLocation(CcHardware.MOD_ID, "computer_case"),
                FabricBlockEntityTypeBuilder.create(
                        ComputerCaseBlockEntity::new,
                        ModBlocks.COMPUTER_CASE
                ).build()
        );
    }
}