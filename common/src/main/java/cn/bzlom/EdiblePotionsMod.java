package cn.bzlom;

import cn.bzlom.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EdiblePotionsMod {
    public static final String MOD_ID = "ediblepotions";
    private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        // 初始化
        ModItems.register();
        ModCreativeTabs.register();
        ModBlocks.register();
        ModBlockEntities.register();
        ModMenuTypes.register();

        LOGGER.info("{} has been initialized!", MOD_ID);
    }
}
