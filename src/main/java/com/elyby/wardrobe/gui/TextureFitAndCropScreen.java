package com.elyby.wardrobe.gui;

import com.elyby.wardrobe.textures.CapeAndElytraManager;
import com.elyby.wardrobe.textures.GifDecoderHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class TextureFitAndCropScreen extends Screen {
    private final Screen parent;
    private final File selectedFile;
    private int offsetX = 0;
    private int offsetY = 0;
    private float scale = 1.0f;
    private boolean isBlackBg = true;

    public TextureFitAndCropScreen(Screen parent, File selectedFile) {
        super(Text.translatable("button.elyby_wardrobe.fit"));
        this.parent = parent;
        this.selectedFile = selectedFile;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("<- Зсув"), b -> offsetX -= 2)
                .dimensions(centerX - 160, this.height - 75, 60, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Зсув ->"), b -> offsetX += 2)
                .dimensions(centerX - 95, this.height - 75, 60, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("+ Масштаб"), b -> scale += 0.1f)
                .dimensions(centerX - 30, this.height - 75, 75, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("- Масштаб"), b -> scale = Math.max(0.1f, scale - 0.1f))
                .dimensions(centerX + 50, this.height - 75, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal(isBlackBg ? "Фон: Чорний" : "Фон: Білий"), b -> {
            isBlackBg = !isBlackBg;
            b.setMessage(Text.literal(isBlackBg ? "Фон: Чорний" : "Фон: Білий"));
        }).dimensions(centerX + 130, this.height - 75, 90, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Застосувати"), b -> applyTexture())
                .dimensions(centerX - 105, this.height - 35, 100, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Назад"), b -> {
            if (this.client != null) this.client.setScreen(parent);
        }).dimensions(centerX + 5, this.height - 35, 100, 20).build());
    }

    private void applyTexture() {
        if (selectedFile != null && selectedFile.exists() && this.client != null && this.client.player != null) {
            String name = selectedFile.getName().toLowerCase();
            if (name.endsWith(".gif")) {
                List<Identifier> frames = GifDecoderHelper.loadGifFrames(selectedFile, offsetX, offsetY, scale, isBlackBg);
                if (!frames.isEmpty()) {
                    CapeAndElytraManager.setPlayerCapeFrames(this.client.player.getUuid(), frames);
                }
            } else {
                try {
                    BufferedImage raw = ImageIO.read(selectedFile);
                    BufferedImage processed = CapeAndElytraManager.processCustomImage(raw, offsetX, offsetY, scale, isBlackBg);
                    Identifier id = CapeAndElytraManager.registerBufferedImage(processed);
                    CapeAndElytraManager.setPlayerCapeFrames(this.client.player.getUuid(), Collections.singletonList(id));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, "Налаштування позиції та розміру на плащі", this.width / 2, 32, 0xAAAAAA);

        int previewX = this.width / 2 - 64;
        int previewY = this.height / 2 - 64;
        context.fill(previewX, previewY, previewX + 128, previewY + 128, isBlackBg ? 0xFF000000 : 0xFFFFFFFF);
        context.drawBorder(previewX, previewY, 128, 128, 0xFF00FF00);

        context.drawCenteredTextWithShadow(this.textRenderer, "X: " + offsetX + " | Y: " + offsetY + " | Zoom: " + String.format("%.1f", scale) + "x", this.width / 2, previewY + 135, 0xFFFF55);
        super.render(context, mouseX, mouseY, delta);
    }
}
