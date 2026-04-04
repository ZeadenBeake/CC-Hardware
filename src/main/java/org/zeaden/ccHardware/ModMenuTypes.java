package org.zeaden.ccHardware;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import org.zeaden.ccHardware.menu.NormalCaseMenu;
import org.zeaden.ccHardware.menu.AdvancedCaseMenu;

public class ModMenuTypes {
    public static MenuType<NormalCaseMenu> NORMAL_CASE_MENU;
    public static MenuType<AdvancedCaseMenu> ADVANCED_CASE_MENU;

    public static void initialize() {
        NORMAL_CASE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                new ResourceLocation(CcHardware.MOD_ID, "normal_case"),
                new ExtendedScreenHandlerType<>(NormalCaseMenu::new)
        );
        ADVANCED_CASE_MENU = Registry.register(
                BuiltInRegistries.MENU,
                new ResourceLocation(CcHardware.MOD_ID, "advanced_case"),
                new ExtendedScreenHandlerType<>(AdvancedCaseMenu::new)
        );
    }
}