package net.moran.loratadine.ui.clickgui;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.setting.impl.BooleanSetting;
import net.moran.loratadine.setting.impl.ModeSetting;
import net.moran.loratadine.setting.impl.NumberSetting;
import net.moran.loratadine.ui.clickgui.values.Component;
import net.moran.loratadine.ui.clickgui.values.impl.BoolValueComponent;
import net.moran.loratadine.ui.clickgui.values.impl.ModeValueComponent;
import net.moran.loratadine.ui.clickgui.values.impl.NumberValueComponent;
import net.moran.loratadine.utils.render.ColorUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class ModuleRenderer implements Wrapper {
   public Module module;
   public Frame parent;
   public int offset;
   public List<Component> components;
   public boolean extended;
   private static final int COMPONENT_HEIGHT = 20;
   private float openProgress;
   private long lastToggleTime;

   public ModuleRenderer(Module module, Frame parent, int offset) {
      this.module = module;
      this.parent = parent;
      this.offset = offset;
      this.extended = false;
      this.openProgress = 0.0F;
      this.lastToggleTime = 0L;
      this.components = new ArrayList<>();
      int valueOffset = 20;

      for (Setting<?> value : module.getSettings()) {
         if (value instanceof BooleanSetting) {
            this.components.add(new BoolValueComponent(value, this, valueOffset));
         } else if (value instanceof ModeSetting) {
            this.components.add(new ModeValueComponent(value, this, valueOffset));
         } else if (value instanceof NumberSetting) {
            this.components.add(new NumberValueComponent(value, this, valueOffset));
         }

         valueOffset += 20;
      }
   }

   public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
      this.updateAnimation();
      ClickGUI.drawRoundedRect(
         stack, x, y, width, height, 0, ColorUtils.color(0, 0, 0, this.isHovered((double)mouseX, (double)mouseY, x, y, width, height) ? 200 : 160)
      );
      int textOffset = 10 - 9 / 2;
      String moduleName = this.module.getName();
      float scaleFactor = 1.0F;
      int moduleNameWidth = mc.font.width(moduleName);
      if (moduleNameWidth > width - 30) {
         scaleFactor = (float)(width - 30) / (float)moduleNameWidth;
      }

      stack.pushPose();
      stack.translate((double)(x + textOffset), (double)(y + textOffset), 0.0);
      stack.scale(scaleFactor, scaleFactor, 1.0F);
      mc.font.drawShadow(stack, moduleName, 0.0F, 0.0F, this.module.isEnabled() ? Color.GREEN.getRGB() : -1);
      stack.popPose();
      if (!this.components.isEmpty()) {
         mc.font.drawShadow(stack, this.extended ? "-" : "+", (float)(x + width - 14), (float)(y + textOffset), -1);
      }

      if (this.openProgress > 0.0F) {
         int componentY = y + 20;

         for (Component component : this.components) {
            if (componentY + 20 > y + height) {
               break;
            }

            component.render(stack, mouseX, mouseY, delta, x, componentY, width, 20);
            componentY += 20;
         }
      }
   }

   private void updateAnimation() {
      long currentTime = System.currentTimeMillis();
      float targetProgress = this.extended ? 1.0F : 0.0F;
      if (this.openProgress != targetProgress) {
         float deltaTime = (float)(currentTime - this.lastToggleTime) / 1000.0F;
         this.openProgress = this.extended ? Math.min(1.0F, this.openProgress + deltaTime) : Math.max(0.0F, this.openProgress - deltaTime);
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (this.isHovered(mouseX, mouseY, this.parent.x, this.parent.y + this.offset, this.parent.width, 20)) {
         if (mouseButton == 0) {
            this.module.setEnabled(!this.module.isEnabled());
         } else if (mouseButton == 1 && !this.components.isEmpty()) {
            this.extended = !this.extended;
            this.lastToggleTime = System.currentTimeMillis();
            this.parent.updateButtons();
         }
      }

      if (this.extended) {
         for (Component component : this.components) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
         }
      }
   }

   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
      if (this.extended) {
         for (Component component : this.components) {
            component.mouseReleased(mouseX, mouseY, mouseButton);
         }
      }
   }

   public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
      return mouseX > (double)x && mouseX < (double)(x + width) && mouseY > (double)y && mouseY < (double)(y + height);
   }

   public int getHeight() {
      return 20 + (this.extended ? this.components.size() * 20 : 0);
   }
}
