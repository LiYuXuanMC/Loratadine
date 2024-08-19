package net.moran.loratadine.ui.clickgui.values.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.setting.impl.BooleanSetting;
import net.moran.loratadine.ui.clickgui.ModuleRenderer;
import net.moran.loratadine.ui.clickgui.values.Component;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class BoolValueComponent extends Component implements Wrapper {
   private BooleanSetting booleanValue = (BooleanSetting)this.value;

   public BoolValueComponent(Setting<?> value, ModuleRenderer parent, int offset) {
      super(value, parent, offset);
      this.booleanValue = (BooleanSetting)value;
   }

   @Override
   public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
      super.render(stack, mouseX, mouseY, delta, x, y, width, height);
      String text = this.booleanValue.getName() + ": " + this.booleanValue.getValue();
      int textWidth = mc.font.width(text);
      mc.font.drawShadow(stack, text, (float)(x + 5), (float)(y + (height / 2 - 9 / 2)), this.booleanValue.getValue() ? 5635925 : 16733525);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (this.isHovered(
            mouseX, mouseY, this.parent.parent.x, this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
         )
         && mouseButton == 0) {
         this.booleanValue.setValue(Boolean.valueOf(!this.booleanValue.getValue()));
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
      super.mouseReleased(mouseX, mouseY, mouseButton);
   }
}
