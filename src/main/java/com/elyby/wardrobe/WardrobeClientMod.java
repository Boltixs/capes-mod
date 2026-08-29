package com.elyby.wardrobe;

import com.elyby.wardrobe.textures.CapeAndElytraManager;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WardrobeClientMod implements ClientModInitializer {
    public static final String MOD_ID = "elyby_wardrobe";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("[Ely.by Wardrobe] Мод успішно завантажено та ініціалізовано.");
        CapeAndElytraManager.init();
    }
}
