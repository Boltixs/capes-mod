package com.elyby.wardrobe.mixin;

import com.elyby.wardrobe.textures.CapeAndElytraManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void overrideSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) (Object) this;
        SkinTextures original = cir.getReturnValue();

        if (CapeAndElytraManager.isElyByAccount(player.getUuid())) {
            long time = player.getWorld() != null ? player.getWorld().getTime() : 0L;
            Identifier customCape = CapeAndElytraManager.getActiveCape(player.getUuid(), time);
            Identifier customElytra = CapeAndElytraManager.getActiveElytra(player.getUuid());

            Identifier finalCape = (customCape != null) ? customCape : original.capeTexture();
            Identifier finalElytra = (customElytra != null) ? customElytra : original.elytraTexture();

            SkinTextures modified = new SkinTextures(
                    original.texture(),
                    original.textureUrl(),
                    finalCape,
                    finalElytra,
                    original.model(),
                    original.secure()
            );
            cir.setReturnValue(modified);
        }
    }
}
