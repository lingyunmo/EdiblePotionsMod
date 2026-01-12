package cn.bzlom;

import cn.bzlom.registry.*;


public final class EdiblePotionsMod {
    public static final String MOD_ID = "ediblepotions";

    public static void init() {
        //初始化
        ModItems.register();
        ModCreativeTabs.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModMenuTypes.register();

        System.out.println(MOD_ID + " has been initialized!");
    }
}