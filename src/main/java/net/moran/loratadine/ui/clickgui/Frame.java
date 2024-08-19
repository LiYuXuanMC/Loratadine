package net.moran.loratadine.ui.clickgui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.moran.loratadine.Loratadine;
import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.ui.clickgui.values.Component;
import net.moran.loratadine.utils.render.ColorUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class Frame implements Wrapper {
   public int x;
   public int y;
   public int dragX;
   public int dragY;
   public int width;
   public int height;
   public Category category;
   public boolean dragging;
   public boolean extended;
   private List<ModuleRenderer> renderers;
   private float openProgress;
   public static final int ANIMATION_DURATION = 1000;
   private long lastToggleTime;
   private ResourceLocation categoryIcon;

   public Frame(int x, int y, int width, int height, Category category) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.category = category;
      this.dragging = false;
      this.extended = false;
      this.openProgress = 0.0F;
      this.lastToggleTime = 0L;
      this.categoryIcon = new ResourceLocation("heypixel", "sb/textures/gui/" + category.name().toLowerCase() + "_icon.png");
      this.renderers = new ArrayList<>();
      int offset = height;

      for (Module mod : Loratadine.INSTANCE.getModuleManager().getModule(category)) {
         this.renderers.add(new ModuleRenderer(mod, this, offset));
         offset += height;
      }
   }

   public void render(PoseStack stack, int mouseX, int mouseY, float delta, float animationProgress) {
      this.updateAnimation();
      ClickGUI.drawRoundedRect(stack, this.x, this.y, this.width, this.height, 0, ColorUtils.color(0, 0, 0, 150));
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
      RenderSystem.setShaderTexture(0, this.categoryIcon);
      Gui.blit(stack, this.x + 5, this.y + 5, 0.0F, 0.0F, 16, 16, 16, 16);
      mc.font.drawShadow(stack, this.category.name, (float)(this.x + 25), (float)this.y + ((float)this.height / 2.0F - 9.0F / 2.0F), -1);
      mc.font.drawShadow(stack, this.extended ? "-" : "+", (float)(this.x + this.width - 14), (float)this.y + ((float)this.height / 2.0F - 9.0F / 2.0F), -1);
      long currentTime = System.currentTimeMillis();
      float targetProgress = this.extended ? 1.0F : 0.0F;
      if (this.openProgress != targetProgress) {
         float deltaTime = (float)(currentTime - this.lastToggleTime) / 1000.0F;
         this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
      }

      if (this.openProgress > 0.0F) {
         int yOffset = this.height;

         for (ModuleRenderer renderer : this.renderers) {
            int moduleHeight = (int)((float)renderer.getHeight() * this.openProgress);
            renderer.render(stack, mouseX, mouseY, delta, this.x, this.y + yOffset, this.width, moduleHeight);
            yOffset += renderer.getHeight();
         }
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (this.isHovered(mouseX, mouseY)) {
         if (mouseButton == 0) {
            this.dragging = true;
            this.dragX = (int)(mouseX - (double)this.x);
            this.dragY = (int)(mouseY - (double)this.y);
         } else if (mouseButton == 1) {
            this.extended = !this.extended;
            this.lastToggleTime = System.currentTimeMillis();
         }
      }

      if (this.extended) {
         for (ModuleRenderer renderer : this.renderers) {
            renderer.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }
   }

   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
      for (ModuleRenderer renderer : this.renderers) {
         renderer.mouseReleased(mouseX, mouseY, mouseButton);
      }

      if (mouseButton == 0 && this.dragging) {
         this.dragging = false;
      }
   }

   public boolean isHovered(double mouseX, double mouseY) {
      return mouseX > (double)this.x && mouseX < (double)(this.x + this.width) && mouseY > (double)this.y && mouseY < (double)(this.y + this.height);
   }

   public void updatePosition(double mouseX, double mouseY) {
      if (this.dragging) {
         this.x = (int)(mouseX - (double)this.dragX);
         this.y = (int)(mouseY - (double)this.dragY);
      }
   }

   public void updateButtons() {
      int offset = this.height;

      for (ModuleRenderer renderer : this.renderers) {
         renderer.offset = offset;
         offset += this.height;
         if (renderer.extended) {
            for (Component component : renderer.components) {
               if (component.value.getHideIf().hide()) {
                  offset += this.height;
               }
            }
         }
      }
   }

   public void updateAnimation() {
      long currentTime = System.currentTimeMillis();
      float targetProgress = this.extended ? 1.0F : 0.0F;
      if (this.openProgress != targetProgress) {
         float deltaTime = (float)(currentTime - this.lastToggleTime) / 1000.0F;
         this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
      }

      this.updateButtons();
   }

   public void onClose() {
      this.extended = false;
      this.openProgress = 0.0F;
   }
}
