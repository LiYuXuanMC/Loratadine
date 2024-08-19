package net.moran.loratadine.ui.clickgui.values;

import com.mojang.blaze3d.vertex.PoseStack;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.ui.clickgui.ClickGUI;
import net.moran.loratadine.ui.clickgui.ModuleRenderer;
import net.moran.loratadine.utils.render.ColorUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class Component implements Wrapper {
   public Setting<?> value;
   public ModuleRenderer parent;
   public int offset;

   public Component(Setting<?> value, ModuleRenderer parent, int offset) {
      this.value = value;
      this.parent = parent;
      this.offset = offset;
   }

   public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
      ClickGUI.drawRoundedRect(
         stack, x, y, width, height, 3, ColorUtils.color(0, 0, 0, this.isHovered((double)mouseX, (double)mouseY, x, y, width, height) ? 180 : 140)
      );
      int textOffset = height / 2 - 9 / 2;
      mc.font.drawShadow(stack, this.value.getName(), (float)(x + 5), (float)(y + textOffset), -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
   }

   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
   }

   public boolean isHovered(double mouseX, double mouseY, int x, int y, int width, int height) {
      return mouseX > (double)x && mouseX < (double)(x + width) && mouseY > (double)y && mouseY < (double)(y + height);
   }
}
