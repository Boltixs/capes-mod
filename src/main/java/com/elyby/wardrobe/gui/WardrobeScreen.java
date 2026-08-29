package com.elyby.wardrobe.gui;

import com.elyby.wardrobe.textures.CapeAndElytraManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.io.File;

public class WardrobeScreen extends Screen {
    private enum Tab { CAPES, ELYTRA }
    private Tab currentTab = Tab.CAPES;

    public WardrobeScreen() {
        super(Text.translatable("screen.elyby_wardrobe.title"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.elyby_wardrobe.capes"), button -> {
            this.currentTab = Tab.CAPES;
        }).dimensions(centerX - 110, 20, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.elyby_wardrobe.elytra"), button -> {
            this.currentTab = Tab.ELYTRA;
        }).dimensions(centerX + 10, 20, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.elyby_wardrobe.upload"), button -> {
            File dummy = new File("custom_cape.png");
            if (this.client != null) {
                this.client.setScreen(new TextureFitAndCropScreen(this, dummy));
            }
        }).dimensions(20, this.height - 35, 170, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Закрити"), button -> {
            if (this.client != null) {
                this.client.setScreen(null);
            }
        }).dimensions(this.width - 110, this.height - 35, 90, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 6, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Ely.by Auth: Авторизовано", 15, 8, 0x55FF55);

        if (this.client != null && this.client.player != null) {
            int playerX = this.width / 4;
            int playerY = this.height / 2 + 70;
            InventoryScreen.drawEntity(context, playerX - 35, playerY - 120, playerX + 35, playerY, 45, 0.0625f, mouseX, mouseY, this.client.player);
        }

        renderCatalog(context, mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
    }

    private void renderCatalog(DrawContext context, int mouseX, int mouseY) {
        int startX = this.width / 2 - 20;
        int startY = 55;
        context.drawTextWithShadow(this.textRenderer, "Каталог спільноти Ely.by (" + (currentTab == Tab.CAPES ? "Плащі" : "Елітри") + "):", startX, startY, 0xFFDD88);

        for (int i = 0; i < 6; i++) {
            int row = i / 3;
            int col = i % 3;
            int cardX = startX + (col * 75);
            int cardY = startY + 20 + (row * 85);

            context.fill(cardX, cardY, cardX + 68, cardY + 78, 0x88000000);
            context.drawBorder(cardX, cardY, 68, 78, 0xFF888888);
            context.drawTextWithShadow(this.textRenderer, "Skin #" + (i + 1), cardX + 5, cardY + 5, 0xCCCCCC);
        }
    }
}
