package cn.bzlom.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import cn.bzlom.EdiblePotionsMod;

@Mod(EdiblePotionsMod.MOD_ID)
public final class EdiblePotionsForge {
    public EdiblePotionsForge() {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(EdiblePotionsMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        // Run our common setup.
        EdiblePotionsMod.init();
    }
}
