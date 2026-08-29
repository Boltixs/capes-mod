package com.elyby.wardrobe.textures;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CapeAndElytraManager {
    private static final Map<UUID, List<Identifier>> ANIMATED_CAPES = new ConcurrentHashMap<>();
    private static final Map<UUID, Identifier> ELYTRA_TEXTURES = new ConcurrentHashMap<>();
    private static final Set<UUID> ELYBY_PLAYERS = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static int textureCounter = 0;

    public static void init() {
    }

    public static boolean isElyByAccount(UUID playerUuid) {
        return ELYBY_PLAYERS.contains(playerUuid);
    }

    public static void registerUserWithElyBy(UUID playerUuid) {
        ELYBY_PLAYERS.add(playerUuid);
    }

    public static void setPlayerCapeFrames(UUID playerUuid, List<Identifier> frames) {
        ANIMATED_CAPES.put(playerUuid, frames);
        ELYBY_PLAYERS.add(playerUuid);
    }

    public static void setPlayerElytra(UUID playerUuid, Identifier elytra) {
        ELYTRA_TEXTURES.put(playerUuid, elytra);
        ELYBY_PLAYERS.add(playerUuid);
    }

    public static Identifier getActiveCape(UUID playerUuid, long gameTime) {
        List<Identifier> frames = ANIMATED_CAPES.get(playerUuid);
        if (frames == null || frames.isEmpty()) {
            return null;
        }
        int index = (int) ((gameTime / 3) % frames.size());
        return frames.get(index);
    }

    public static Identifier getActiveElytra(UUID playerUuid) {
        return ELYTRA_TEXTURES.get(playerUuid);
    }

    public static Identifier registerBufferedImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        NativeImage nativeImage = new NativeImage(width, height, false);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = img.getRGB(x, y);
                nativeImage.setColorArgb(x, y, argb);
            }
        }

        NativeImageBackedTexture texture = new NativeImageBackedTexture(nativeImage);
        Identifier id = Identifier.of("elyby_wardrobe", "custom_texture_" + (++textureCounter));
        MinecraftClient.getInstance().getTextureManager().registerTexture(id, texture);
        return id;
    }

    public static BufferedImage processCustomImage(BufferedImage input, int offsetX, int offsetY, float scale, boolean isBlackBackground) {
        int targetWidth = 64;
        int targetHeight = 32;
        BufferedImage result = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = result.createGraphics();

        // Заливка фону (чорний або білий)
        if (isBlackBackground) {
            g.setColor(java.awt.Color.BLACK);
        } else {
            g.setColor(java.awt.Color.WHITE);
        }
        g.fillRect(0, 0, targetWidth, targetHeight);

        // Накладання масштабованого та зсунутого зображення
        int scaledW = (int) (input.getWidth() * scale);
        int scaledH = (int) (input.getHeight() * scale);
        g.drawImage(input, offsetX, offsetY, scaledW, scaledH, null);
        g.dispose();

        return result;
    }
}
