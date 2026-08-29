package com.elyby.wardrobe.textures;

import net.minecraft.util.Identifier;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GifDecoderHelper {

    public static List<Identifier> loadGifFrames(File gifFile, int offsetX, int offsetY, float scale, boolean blackBg) {
        List<Identifier> ids = new ArrayList<>();
        try (ImageInputStream stream = ImageIO.createImageInputStream(gifFile)) {
            Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
            if (!readers.hasNext()) return ids;

            ImageReader reader = readers.next();
            reader.setInput(stream);

            int numFrames = reader.getNumImages(true);
            for (int i = 0; i < numFrames; i++) {
                BufferedImage rawFrame = reader.read(i);
                BufferedImage fittedFrame = CapeAndElytraManager.processCustomImage(rawFrame, offsetX, offsetY, scale, blackBg);
                Identifier textureId = CapeAndElytraManager.registerBufferedImage(fittedFrame);
                ids.add(textureId);
            }
            reader.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }
}
