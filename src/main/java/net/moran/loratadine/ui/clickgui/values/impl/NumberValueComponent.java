package net.moran.loratadine.ui.clickgui.values.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import net.moran.loratadine.setting.Setting;
import net.moran.loratadine.setting.impl.NumberSetting;
import net.moran.loratadine.ui.clickgui.ModuleRenderer;
import net.moran.loratadine.ui.clickgui.values.Component;
import net.moran.loratadine.utils.math.MathUtils;
import net.moran.loratadine.utils.wrapper.Wrapper;

public class NumberValueComponent extends Component implements Wrapper {
   private NumberSetting numValue = (NumberSetting)this.value;
   private boolean sliding = false;

   public NumberValueComponent(Setting<?> value, ModuleRenderer parent, int offset) {
      super(value, parent, offset);
      this.numValue = (NumberSetting)value;
   }

   @Override
   public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
      super.render(stack, mouseX, mouseY, delta, x, y, width, height);
      int renderWidth = (int)(
         (float)width
            * (this.numValue.getValue().floatValue() - this.numValue.getMinValue().floatValue())
            / (this.numValue.getMaxValue().floatValue() - this.numValue.getMinValue().floatValue())
      );
      Gui.fill(stack, x, y + height - 3, x + renderWidth, y + height, Color.WHITE.getRGB());
      if (this.sliding) {
         double diff = (double)Math.min(width, Math.max(0, mouseX - x));
         this.numValue
            .setValue(
               Double.valueOf(
                  MathUtils.roundToPlace(
                     diff / (double)width * (double)(this.numValue.getMaxValue().floatValue() - this.numValue.getMinValue().floatValue())
                        + (double)this.numValue.getMinValue().floatValue(),
                     2
                  )
               )
            );
      }

      String text = this.numValue.getName() + ": " + MathUtils.roundToPlace((double)this.numValue.getValue().floatValue(), 2);
      mc.font.drawShadow(stack, text, (float)(x + 5), (float)(y + (height / 2 - 9 / 2)), -1);
   }

   @Override
   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (this.isHovered(
            mouseX, mouseY, this.parent.parent.x, this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
         )
         && mouseButton == 0) {
         this.sliding = true;
      }
   }

   @Override
   public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
      this.sliding = false;
   }
}
