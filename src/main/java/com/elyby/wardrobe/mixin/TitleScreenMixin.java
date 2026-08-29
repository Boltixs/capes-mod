package com.elyby.wardrobe.mixin;

import com.elyby.wardrobe.gui.WardrobeScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void injectWardrobeButton(CallbackInfo ci) {
        int buttonX = this.width / 4 - 50;
        int buttonY = this.height / 2 + 55;

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("button.elyby_wardrobe.wardrobe"), button -> {
            if (this.client != null) {
                this.client.setScreen(new WardrobeScreen());
            }
        }).dimensions(buttonX, buttonY, 100, 20).build());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void renderPlayerInMainMenu(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client != null && this.client.player != null) {
            int playerX = this.width / 4;
            int playerY = this.height / 2 + 45;
            InventoryScreen.drawEntity(context, playerX - 30, playerY - 100, playerX + 30, playerY, 40, 0.0625f, mouseX, mouseY, this.client.player);
        }
    }
}
