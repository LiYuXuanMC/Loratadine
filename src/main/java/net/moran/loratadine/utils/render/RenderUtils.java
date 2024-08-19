package net.moran.loratadine.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class RenderUtils implements Wrapper {
   public static void renderEntityBoundingBox(PoseStack poseStack, int type, Entity entity, int color, boolean damage) {
      if (entity instanceof LivingEntity) {
         EntityRenderDispatcher renderManager = mc.getEntityRenderDispatcher();
         double x = entity.xOld + (entity.getX() - entity.xOld) * (double)mc.getFrameTime() - renderManager.camera.getPosition().x();
         double y = entity.yOld + (entity.getY() - entity.yOld) * (double)mc.getFrameTime() - renderManager.camera.getPosition().y();
         double z = entity.zOld + (entity.getZ() - entity.zOld) * (double)mc.getFrameTime() - renderManager.camera.getPosition().z();
         float scale = 0.03F;
         if (entity instanceof Player && damage && ((Player)entity).hurtTime > 0) {
            color = Color.RED.getRGB();
         }

         RenderSystem.disableDepthTest();
         switch (type) {
            case 0:
               poseStack.pushPose();
               poseStack.translate(x, y, z);
               poseStack.mulPose(new Quaternion(0.0F, -renderManager.camera.getYRot(), 0.0F, true));
               poseStack.scale(scale, scale, scale);
               int outline = Color.BLACK.getRGB();
               drawRect(poseStack, -20, -1, -26, 75, outline);
               drawRect(poseStack, 20, -1, 26, 75, outline);
               drawRect(poseStack, -20, -1, 21, 5, outline);
               drawRect(poseStack, -20, 70, 21, 75, outline);
               if (color != 0) {
                  drawRect(poseStack, -21, 0, -25, 74, color);
                  drawRect(poseStack, 21, 0, 25, 74, color);
                  drawRect(poseStack, -21, 0, 24, 4, color);
                  drawRect(poseStack, -21, 71, 25, 74, color);
               } else {
                  int startColor = rainbowDraw(2L, 0L);
                  int endColor = rainbowDraw(2L, 1000L);
                  drawGradientRect(poseStack, -21, 0, -25, 74, startColor, endColor);
                  drawGradientRect(poseStack, 21, 0, 25, 74, startColor, endColor);
                  drawRect(poseStack, -21, 0, 21, 4, endColor);
                  drawRect(poseStack, -21, 71, 21, 74, startColor);
               }

               RenderSystem.enableDepthTest();
               poseStack.popPose();
         }
      }
   }

   public static int rainbowDraw(long speed, long... delay) {
      long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
      return Color.getHSBColor((float)(time % (15000L / speed)) / (15000.0F / (float)speed), 1.0F, 1.0F).getRGB();
   }

   public static void drawRect(PoseStack poseStack, int left, int top, int right, int bottom, int color) {
      if (left < right) {
         int j = left;
         left = right;
         right = j;
      }

      if (top < bottom) {
         int j = top;
         top = bottom;
         bottom = j;
      }

      Matrix4f matrix = poseStack.last().pose();
      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuilder();
      RenderSystem.setShader(new Supplier<ShaderInstance>() {
         public ShaderInstance get() {
            return GameRenderer.getPositionColorShader();
         }
      });
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.disableTexture();
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      bufferbuilder.vertex(matrix, (float)left, (float)bottom, 0.0F).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
      bufferbuilder.vertex(matrix, (float)right, (float)bottom, 0.0F)
         .color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF)
         .endVertex();
      bufferbuilder.vertex(matrix, (float)right, (float)top, 0.0F).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
      bufferbuilder.vertex(matrix, (float)left, (float)top, 0.0F).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >> 24 & 0xFF).endVertex();
      tessellator.end();
      RenderSystem.enableTexture();
   }

   public static void drawGradientRect(PoseStack poseStack, int left, int top, int right, int bottom, int startColor, int endColor) {
      Matrix4f matrix = poseStack.last().pose();
      Tesselator tessellator = Tesselator.getInstance();
      BufferBuilder bufferbuilder = tessellator.getBuilder();
      RenderSystem.setShader(new Supplier<ShaderInstance>() {
         public ShaderInstance get() {
            return GameRenderer.getPositionColorShader();
         }
      });
      RenderSystem.disableTexture();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      float startAlpha = (float)(startColor >> 24 & 0xFF) / 255.0F;
      float startRed = (float)(startColor >> 16 & 0xFF) / 255.0F;
      float startGreen = (float)(startColor >> 8 & 0xFF) / 255.0F;
      float startBlue = (float)(startColor & 0xFF) / 255.0F;
      float endAlpha = (float)(endColor >> 24 & 0xFF) / 255.0F;
      float endRed = (float)(endColor >> 16 & 0xFF) / 255.0F;
      float endGreen = (float)(endColor >> 8 & 0xFF) / 255.0F;
      float endBlue = (float)(endColor & 0xFF) / 255.0F;
      bufferbuilder.begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      bufferbuilder.vertex(matrix, (float)right, (float)top, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
      bufferbuilder.vertex(matrix, (float)left, (float)top, 0.0F).color(startRed, startGreen, startBlue, startAlpha).endVertex();
      bufferbuilder.vertex(matrix, (float)left, (float)bottom, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
      bufferbuilder.vertex(matrix, (float)right, (float)bottom, 0.0F).color(endRed, endGreen, endBlue, endAlpha).endVertex();
      tessellator.end();
      RenderSystem.disableBlend();
      RenderSystem.enableTexture();
   }
}
