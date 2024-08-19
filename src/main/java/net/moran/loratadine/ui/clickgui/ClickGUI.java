package net.moran.loratadine.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.network.chat.Component;
import net.moran.loratadine.modules.Category;

public class ClickGUI extends Screen {
   public static final ClickGUI INSTANCE = new ClickGUI();
   private final List<Frame> frames = new ArrayList<>();
   private static final int ANIMATION_DURATION = 200;
   private long openTime;

   protected ClickGUI() {
      super(Component.nullToEmpty("Click GUI"));
      int offset = 20;

      for (Category category : Category.values()) {
         this.frames.add(new Frame(offset, 20, 120, 25, category));
         offset += 140;
      }
   }

   public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
      this.renderBackground(poseStack);
      long currentTime = System.currentTimeMillis();
      float animationProgress = Math.min(1.0F, (float)(currentTime - this.openTime) / 200.0F);

      for (Frame frame : this.frames) {
         frame.render(poseStack, mouseX, mouseY, partialTicks, animationProgress);
         frame.updatePosition((double)mouseX, (double)mouseY);
      }

      super.render(poseStack, mouseX, mouseY, partialTicks);
   }

   public void onClose() {
      super.onClose();

      for (Frame frame : this.frames) {
         frame.onClose();
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      for (Frame frame : this.frames) {
         frame.mouseReleased(mouseX, mouseY, button);
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      for (Frame frame : this.frames) {
         frame.mouseClicked(mouseX, mouseY, button);
      }

      return super.mouseClicked(mouseX, mouseY, button);
   }

   protected void init() {
      super.init();
      this.openTime = System.currentTimeMillis();
   }

   public static void drawRoundedRect(PoseStack poseStack, int x, int y, int width, int height, int cornerRadius, int color) {
      fill(poseStack, x + cornerRadius, y, x + width - cornerRadius, y + height, color);
      fill(poseStack, x, y + cornerRadius, x + cornerRadius, y + height - cornerRadius, color);
      fill(poseStack, x + width - cornerRadius, y + cornerRadius, x + width, y + height - cornerRadius, color);
      fillCircle(poseStack, x + cornerRadius, y + cornerRadius, cornerRadius, color);
      fillCircle(poseStack, x + width - cornerRadius, y + cornerRadius, cornerRadius, color);
      fillCircle(poseStack, x + cornerRadius, y + height - cornerRadius, cornerRadius, color);
      fillCircle(poseStack, x + width - cornerRadius, y + height - cornerRadius, cornerRadius, color);
   }

   private static void fillCircle(PoseStack poseStack, int centerX, int centerY, int radius, int color) {
      RenderSystem.setShader(new Supplier<ShaderInstance>() {
         public ShaderInstance get() {
            return GameRenderer.getPositionColorShader();
         }
      });
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

      for (int y = -radius; y <= radius; y++) {
         for (int x = -radius; x <= radius; x++) {
            if (x * x + y * y <= radius * radius) {
               poseStack.pushPose();
               poseStack.translate((double)(centerX + x), (double)(centerY + y), 0.0);
               fill(poseStack, 0, 0, 1, 1, color);
               poseStack.popPose();
            }
         }
      }
   }
}
