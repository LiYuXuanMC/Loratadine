package net.moran.loratadine.modules.impl.render;

import net.moran.loratadine.modules.Category;
import net.moran.loratadine.modules.Module;
import net.moran.loratadine.ui.clickgui.ClickGUI;

public class ClickGui extends Module {
   public ClickGui() {
      super("ClickGui", Category.RENDER, 344);
   }

   @Override
   protected void onEnable() {
      mc.setScreen(ClickGUI.INSTANCE);
      this.setEnabled(false);
   }
}
