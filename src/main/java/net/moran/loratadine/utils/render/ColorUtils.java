package net.moran.loratadine.utils.render;

import java.awt.Color;


public final class ColorUtils {
   public static Color rainbow(int speed, int index) {
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      float hue = (float)angle / 360.0F;
      return new Color(Color.HSBtoRGB(hue, 0.7F, 1.0F));
   }

   public static int alpha(int hex) {
      return hex >> 24 & 0xFF;
   }

   public static int red(int hex) {
      return hex >> 16 & 0xFF;
   }

   public static int green(int hex) {
      return hex >> 8 & 0xFF;
   }

   public static int blue(int hex) {
      return hex & 0xFF;
   }

   public static Color getBlack(float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(0.0F, 0.0F, 0.0F, opacity);
   }

   public static int applyOpacity(int color, float opacity) {
      Color old = new Color(color);
      return applyOpacity(old, opacity).getRGB();
   }

   public static Color applyOpacity(Color color, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
   }

   public static int color(int r, int g, int b, int a) {
      return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
   }


   private ColorUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
