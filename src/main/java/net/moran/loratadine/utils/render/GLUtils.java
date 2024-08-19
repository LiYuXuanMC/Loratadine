package net.moran.loratadine.utils.render;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.image.BufferedImage;
import lombok.Generated;
import net.minecraft.client.renderer.texture.DynamicTexture;

public final class GLUtils {
   public static void color(int r, int g, int b, int a) {
      RenderSystem.setShaderColor((float)r / 255.0F, (float)g / 255.0F, (float)b / 255.0F, (float)a / 255.0F);
   }

   public static void color(int hex) {
      color(ColorUtils.red(hex), ColorUtils.green(hex), ColorUtils.blue(hex), ColorUtils.alpha(hex));
   }

   public static int uploadTexture(BufferedImage image) {
      NativeImage nativeImage = new NativeImage(image.getWidth(), image.getHeight(), false);

      for (int x = 0; x < image.getWidth(); x++) {
         for (int y = 0; y < image.getHeight(); y++) {
            nativeImage.setPixelRGBA(x, y, image.getRGB(x, y));
         }
      }

      DynamicTexture texture = new DynamicTexture(nativeImage);
      return texture.getId();
   }

   @Generated
   private GLUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
