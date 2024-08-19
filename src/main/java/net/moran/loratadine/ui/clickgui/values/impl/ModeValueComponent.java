package net.moran.loratadine.ui.clickgui.values.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.setting.impl.ModeSetting;
import net.moran.loratadine.ui.clickgui.ModuleRenderer;
import net.moran.loratadine.ui.clickgui.values.Component;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class ModeValueComponent extends Component implements Wrapper {
   private ModeSetting modeValue = (ModeSetting)this.value;

   public ModeValueComponent(Setting<?> value, ModuleRenderer parent, int offset) {
      super(value, parent, offset);
      this.modeValue = (ModeSetting)value;
   }

   @Override
   public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
      super.render(stack, mouseX, mouseY, delta, x, y, width, height);
      String text = this.modeValue.getName() + ": " + this.modeValue.getValue();
      int textWidth = mc.font.width(text);
      mc.font.drawShadow(stack, text, (float)(x + 5), (float)(y + (height / 2 - 9 / 2)), -1);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (this.isHovered(
            mouseX, mouseY, this.parent.parent.x, this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
         )
         && mouseButton == 0) {
         String[] modes = this.modeValue.getValues();
         int currentIndex = Arrays.asList(modes).indexOf(this.modeValue.getValue());
         int nextIndex = (currentIndex + 1) % modes.length;
         this.modeValue.setValue(modes[nextIndex]);
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
      super.mouseReleased(mouseX, mouseY, mouseButton);
   }
}
